using UnityEngine;
using System.Collections;
using UnityEditor;

[CustomEditor(typeof(Curve))]
public class CurveEditor : Editor
{

    private Quaternion q = new Quaternion();

    void OnSceneGUI()
    {
        Curve cv = (Curve)target;
        
        if (cv.showHandles)
        {
            for (int i = 0; i < cv.points.Length; i++)
            {
                cv.SetCP(i, Handles.PositionHandle(cv.GetCP(i), q));
                if (GUI.changed)
                {
                    cv.GenerateMesh();
                    EditorUtility.SetDirty(target);
                }

            }
        }


    }

}
