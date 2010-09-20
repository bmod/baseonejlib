using UnityEngine;
using System.Collections;


public class ChaseCam : MonoBehaviour {

    public GameObject target;

    private float positionK = .3f;
    private float positionDamp = .6f;

    public Vector3 positionOffset = new Vector3(0, 1, 4);
    private Vector3 positionTarget;
    private Vector3 positionVelocity = new Vector3();

    public Quaternion angle = new Quaternion();

	// Use this for initialization
	void Start () {
        positionTarget = transform.position;
	}

    
	
	// Update is called once per frame
	void FixedUpdate () {
        positionVelocity += (target.transform.position - positionTarget) * positionK;
        positionVelocity *= positionDamp;
        positionTarget += positionVelocity;

        // Rotate
        angle = Quaternion.Slerp(angle, target.transform.rotation, .01f);
        Vector3 offset = angle * positionOffset;


        transform.localPosition = positionTarget + offset;
        transform.localRotation = Quaternion.Slerp(transform.localRotation, target.transform.rotation, .2f);
    }
}
