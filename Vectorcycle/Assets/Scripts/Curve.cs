using UnityEngine;
using System.Collections;

public class Curve : MonoBehaviour
{

    public int uSegments = 20;
    public int vSegments = 3;
    public Color debugLinecolor = Color.white;

    public bool showHandles = true;

    public Curve connectToCurve;
    public Vector3[] points = new Vector3[0];


    private static float debugRadius = 1;
    private static Color debugStartPointColor = new Color(.5f, 1, 0);
    private static Color debugEndPointColor = new Color(1, .5f, 0);
    private static Color debugHandleColor = Color.white;
    private static Color debugPointColor = Color.yellow;
    private static Color debugExtensionColor = Color.gray;

    // Use this for initialization
    void Start()
    {

    }

    // Update is called once per frame
    void Update()
    {

    }

    public int CPCount()
    {
        if (connectToCurve != null)
            return points.Length + 3;
        return points.Length;
    }

    public Vector3 GetCP(int i)
    {
        if (i >= points.Length)
            return connectToCurve.GetCP(i - points.Length);
        return points[i] + transform.position;
    }

    public void SetCP(int i, Vector3 v)
    {
        points[i] = v - transform.position;
    }

    public Vector3 GetPoint(float t)
    {
        int numSections = CPCount() - 3;
        int currPt = Mathf.Min(Mathf.FloorToInt(t * (float)numSections), numSections - 1);
        float u = t * (float)numSections - (float)currPt;

        Vector3 a = GetCP(currPt);
        Vector3 b = GetCP(currPt + 1);
        Vector3 c = GetCP(currPt + 2);
        Vector3 d = GetCP(currPt + 3);

        return .5f * (
            (-a + 3f * b - 3f * c + d) * (u * u * u)
            + (2f * a - 5f * b + 4f * c - d) * (u * u)
            + (-a + c) * u
            + 2f * b
        );
    }

    public Quaternion Orientation(float t, float precision, Vector3 up)
    {
        Vector3 tangent;
        if (t + precision <= 1)
        {
            tangent = GetPoint(t + precision) - GetPoint(t);
        }
        else
        {
            tangent = GetPoint(t) - GetPoint(t - precision);
        }
        tangent.Normalize();
        //        Vector3 binormal = Vector3.Cross(tangent, up);
        //        binormal.Normalize();
        //        Vector3 normal = Vector3.Cross(binormal, tangent);
        //        normal.Normalize();
        Quaternion q = new Quaternion();
        q.SetLookRotation(tangent, up);
        return q;
    }

    public Vector3 Velocity(float t)
    {
        int numSections = CPCount() - 3;
        int currPt = Mathf.Min(Mathf.FloorToInt(t * (float)numSections), numSections - 1);
        float u = t * (float)numSections - (float)currPt;

        Vector3 a = GetPoint(currPt);
        Vector3 b = GetPoint(currPt + 1);
        Vector3 c = GetPoint(currPt + 2);
        Vector3 d = GetPoint(currPt + 3);

        return 1.5f * (-a + 3f * b - 3f * c + d) * (u * u)
                + (2f * a - 5f * b + 4f * c - d) * u
                + .5f * c - .5f * a;
    }

    public void GenerateMesh()
    {
        
        float width = 3;
        int numVerts = (uSegments + 1) * (vSegments + 1);
        Vector3[] verts = new Vector3[numVerts];
        Vector3[] normals = new Vector3[numVerts];
        Vector2[] uvs = new Vector2[numVerts];
        int[] tris = new int[uSegments * vSegments * 6];
        
        float uStep = 1f / (float)uSegments;
        float vStep = 1f / (float) vSegments;
        int i = 0;
        int j = 0;
        for (int u = 0; u <= uSegments; u++)
        {
            for (int v = 0; v <= vSegments; v++)
            {
                Vector3 vertex = GetPoint(uStep*u, vStep*v * width);
                verts[i] = vertex;
                normals[i] = Vector3.up;
                uvs[i] = new Vector2(uStep * u, vStep * v);
                if (u < uSegments && v < vSegments)
                {
                    tris[j++] = i;
                    tris[j++] = i + 1;
                    tris[j++] = i + vSegments + 1;

                    tris[j++] = i + 1;
                    tris[j++] = i + vSegments + 2;
                    tris[j++] = i + vSegments + 1;
                }
                i++;
            }
        }

        

        Mesh m = GetSharedMesh();
        m.vertices = verts;
        m.triangles = tris;
        m.uv = uvs;
        m.normals = normals;
        GetMeshRenderer();
    }

    private Vector3 GetPoint(float u, float v)
    {
        Vector3 p = GetPoint(u);
        p.x += v;
        return p;
    }

    private MeshRenderer GetMeshRenderer()
    {
        MeshRenderer mr = GetComponent<MeshRenderer>();
        if (null == mr)
            mr = gameObject.AddComponent<MeshRenderer>();
        return mr;
    }

    private Mesh GetSharedMesh()
    {
        MeshFilter mf = GetComponent<MeshFilter>();
        if (null == mf)
        {
            mf = gameObject.AddComponent<MeshFilter>();
        }
        if (null == mf.sharedMesh)
        {
            mf.sharedMesh = new Mesh();
        }
        return mf.sharedMesh;
    }

    void OnDrawGizmosSelected()
    {
        Gizmos.color = debugHandleColor;
        Gizmos.DrawWireSphere(transform.position, debugRadius * 4);
        Gizmos.color = Color.Lerp(debugStartPointColor, Color.gray, .7f);
        Gizmos.DrawLine(transform.position, GetCP(0));
        Gizmos.color = Color.Lerp(debugEndPointColor, Color.gray, .7f);
        Gizmos.DrawLine(transform.position, GetCP(points.Length - 1));
    }

    void OnDrawGizmos()
    {
        Gizmos.color = debugLinecolor;
        Vector3 curr = GetPoint(0);
        Vector3 prev = curr;
        float step = 1f / (float)uSegments;
        for (int i = 0; i <= uSegments; i++)
        {
            curr = GetPoint(i * step);
            Gizmos.DrawLine(curr, prev);
            prev = curr;
        }

        Gizmos.color = debugStartPointColor;
        for (int i = 0; i < points.Length; i++)
        {
            if (i == points.Length - 1)
            {
                Gizmos.color = debugEndPointColor;
            }
            else if (i > 0)
            {
                Gizmos.color = debugPointColor;
            }

            if (i == 1)
            {
                Gizmos.DrawWireSphere(GetCP(i), debugRadius * 3);
            }
            else
            {
                Gizmos.DrawWireSphere(GetCP(i), debugRadius);
            }
        }


        Gizmos.color = debugExtensionColor;
        Gizmos.DrawLine(GetCP(0), GetCP(1));
        if (null == connectToCurve)
            Gizmos.DrawLine(GetCP(points.Length - 1), GetCP(points.Length - 2));

    }

}
