using UnityEngine;
using System.Collections;

[ExecuteInEditMode]
public class Vehicle : MonoBehaviour {

    private float currentSpeed = .003f;

    public Curve startCurve;

    public float pos = 0;

	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void FixedUpdate () {
        if (null != startCurve) {
            transform.localPosition = startCurve.GetPoint(pos);
            transform.localRotation = startCurve.Orientation(pos, .001f, Vector3.up);
        }
        pos += currentSpeed;
        if (pos > 1)
        {
            pos -= 1;
            if (startCurve.connectToCurve != null)
            {
                startCurve = startCurve.connectToCurve;
            }
        }
	}
}
