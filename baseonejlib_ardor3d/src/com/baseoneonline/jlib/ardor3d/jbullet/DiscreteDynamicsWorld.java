package com.baseoneonline.jlib.ardor3d.jbullet;

import java.awt.Shape;
import java.util.Stack;
import java.util.logging.Logger;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.BroadphaseNativeType;
import com.bulletphysics.collision.broadphase.Dispatcher;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.shapes.CapsuleShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.CompoundShape;
import com.bulletphysics.collision.shapes.ConeShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.linearmath.Transform;

/*
 * Java port of Bullet (c) 2008 Martin Dvorak <jezek2@advel.cz>
 *
 * Bullet Continuous Collision Detection and Physics Library
 * Copyright (c) 2003-2008 Erwin Coumans  http://www.bulletphysics.com/
 *
 * This software is provided 'as-is', without any express or implied warranty.
 * In no event will the authors be held liable for any damages arising from
 * the use of this software.
 * 
 * Permission is granted to anyone to use this software for any purpose, 
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 * 
 * 1. The origin of this software must not be misrepresented; you must not
 *    claim that you wrote the original software. If you use this software
 *    in a product, an acknowledgment in the product documentation would be
 *    appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 *    misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */

/**
 * DiscreteDynamicsWorld provides discrete rigid body simulation.
 * 
 * @author jezek2
 */
public class DiscreteDynamicsWorld extends
		com.bulletphysics.dynamics.DiscreteDynamicsWorld {

	public DiscreteDynamicsWorld(Dispatcher dispatcher,
			BroadphaseInterface pairCache, ConstraintSolver constraintSolver,
			CollisionConfiguration collisionConfiguration) {
		super(dispatcher, pairCache, constraintSolver, collisionConfiguration);
	}

	@Override
	public void debugDrawObject(Transform worldTransform, CollisionShape shape,
			Vector3f color) {
		Vector3f tmp = new Vector3f();
		Vector3f tmp2 = new Vector3f();
		Transform tmpXf = new Transform();
		float radius;

		// Draw a small simplex at the center of the object
		{
			Vector3f start = new Vector3f(worldTransform.origin);

			tmp.set(1f, 0f, 0f);
			worldTransform.basis.transform(tmp);
			tmp.add(start);
			tmp2.set(1f, 0f, 0f);
			getDebugDrawer().drawLine(start, tmp, tmp2);

			tmp.set(0f, 1f, 0f);
			worldTransform.basis.transform(tmp);
			tmp.add(start);
			tmp2.set(0f, 1f, 0f);
			getDebugDrawer().drawLine(start, tmp, tmp2);

			tmp.set(0f, 0f, 1f);
			worldTransform.basis.transform(tmp);
			tmp.add(start);
			tmp2.set(0f, 0f, 1f);
			getDebugDrawer().drawLine(start, tmp, tmp2);
		}

		// JAVA TODO: debugDrawObject, note that this commented code is from old
		// version, use actual version when implementing

		 if (shape.getShapeType() == BroadphaseNativeType.COMPOUND_SHAPE_PROXYTYPE)
		 {
		 CompoundShape compoundShape = (CompoundShape) shape;
		 for (int i=compoundShape.getNumChildShapes()-1;i>=0;i--)
		 {
			 Transform childTrans = new Transform();
		 compoundShape.getChildTransform(i, childTrans);
		 CollisionShape colShape = compoundShape.getChildShape(i);
		 Transform trans = new Transform();
		 worldTransform.mul(childTrans, trans);
		 debugDrawObject(trans,colShape,color);
		 }
		
		 } else
		 {
		 switch (shape.getShapeType())
		 {
		
		 case SPHERE_SHAPE_PROXYTYPE:
		 SphereShape sphereShape = (SphereShape) shape;
		  radius = sphereShape.getMargin();//radius doesn't include
		 
		
		 debugDrawSphere(radius, worldTransform, color);
		 break;
		 case MULTI_SPHERE_SHAPE_PROXYTYPE:
			 Logger.getLogger(getClass().getName()).warning("No debug draw support for "+BroadphaseNativeType.MULTI_SPHERE_SHAPE_PROXYTYPE);
		
		 break;
		 case CAPSULE_SHAPE_PROXYTYPE:
		 CapsuleShape capsuleShape = (CapsuleShape) shape;
		
		 float radius1 = capsuleShape.getRadius();
		 float halfHeight = capsuleShape.getHalfHeight();
		
		 // Draw the ends
		 
		 tmpXf.set(worldTransform);
		 tmp.set(0,  halfHeight, 0);
		 worldTransform.transform(tmp);
		 debugDrawSphere(radius1, tmpXf, color);
		 
		 
		  tmpXf.set(worldTransform);
			 tmp.set(0,  -halfHeight, 0);
		 tmpXf.origin.set(tmp);
		 debugDrawSphere(radius1, tmpXf, color);
		 
		
		 // Draw some additional lines
		 Vector3f a = new Vector3f();
		 Vector3f b = new Vector3f();
		 
		 a.set(-radius1,halfHeight,0);
		 b.set(-radius1,-halfHeight,0);
		 worldTransform.basis.transform(a);
		 worldTransform.basis.transform(b);
		 a.add(worldTransform.origin);
		 b.add(worldTransform.origin);
		 getDebugDrawer().drawLine(a,b, color);
		 
		 a.set(radius1,halfHeight,0);
		 b.set(radius1,-halfHeight,0);
		 worldTransform.basis.transform(a);
		 worldTransform.basis.transform(b);
		 a.add(worldTransform.origin);
		 b.add(worldTransform.origin);
		 getDebugDrawer().drawLine(a,b, color);
		 
		 a.set(0,halfHeight,-radius1);
		 b.set(0,-halfHeight,-radius1);
		 worldTransform.basis.transform(a);
		 worldTransform.basis.transform(b);
		 a.add(worldTransform.origin);
		 b.add(worldTransform.origin);
		 getDebugDrawer().drawLine(a,b, color);

		 a.set(0,halfHeight,radius1);
		 b.set(0,-halfHeight,radius1);
		 worldTransform.basis.transform(a);
		 worldTransform.basis.transform(b);
		 a.add(worldTransform.origin);
		 b.add(worldTransform.origin);
		 getDebugDrawer().drawLine(a,b, color);

		 break;
		 
		 case CONE_SHAPE_PROXYTYPE:
		 {
		 ConeShape coneShape =(ConeShape) shape;
		  radius = coneShape.getRadius();//+coneShape.getMargin();
		 float height = coneShape.getHeight();//+coneShape.getMargin();
		 Vector3f start = worldTransform.origin;
		
		 int upAxis= coneShape.getConeUpIndex();
		
		
		 Vector3f offsetHeight = new Vector3f(0,0,0);
		 offsetHeight[upAxis] = height * float(0.5);
		 Vector3f offsetRadius(0,0,0);
		 offsetRadius[(upAxis+1)%3] = radius;
		 Vector3f offset2Radius(0,0,0);
		 offset2Radius[(upAxis+2)%3] = radius;
		
		 getDebugDrawer().drawLine(start+worldTransform.getBasis() *
		 (offsetHeight),start+worldTransform.getBasis() *
		 (-offsetHeight+offsetRadius),color);
		 getDebugDrawer().drawLine(start+worldTransform.getBasis() *
		 (offsetHeight),start+worldTransform.getBasis() *
		 (-offsetHeight-offsetRadius),color);
		 getDebugDrawer().drawLine(start+worldTransform.getBasis() *
		 (offsetHeight),start+worldTransform.getBasis() *
		 (-offsetHeight+offset2Radius),color);
		 getDebugDrawer().drawLine(start+worldTransform.getBasis() *
		 (offsetHeight),start+worldTransform.getBasis() *
		 (-offsetHeight-offset2Radius),color);
		
		
		
		 break;
		
		 }
		 case CYLINDER_SHAPE_PROXYTYPE:
		 {
		 btCylinderShape* cylinder = static_cast<const
		 btCylinderShape*>(shape);
		 int upAxis = cylinder.getUpAxis();
		 float radius = cylinder.getRadius();
		 float halfHeight = cylinder.getHalfExtentsWithMargin()[upAxis];
		 Vector3f start = worldTransform.origin;
		 Vector3f offsetHeight(0,0,0);
		 offsetHeight[upAxis] = halfHeight;
		 Vector3f offsetRadius(0,0,0);
		 offsetRadius[(upAxis+1)%3] = radius;
		 getDebugDrawer().drawLine(start+worldTransform.getBasis() *
		 (offsetHeight+offsetRadius),start+worldTransform.getBasis() *
		 (-offsetHeight+offsetRadius),color);
		 getDebugDrawer().drawLine(start+worldTransform.getBasis() *
		 (offsetHeight-offsetRadius),start+worldTransform.getBasis() *
		 (-offsetHeight-offsetRadius),color);
		 break;
		 }
		 default:
		 {
		
		 if (shape.isConcave())
		 {
		 btConcaveShape* concaveMesh = (btConcaveShape*) shape;
		
		 //todo pass camera, for some culling
		 Vector3f aabbMax(float(1e30),float(1e30),float(1e30));
		 Vector3f aabbMin(float(-1e30),float(-1e30),float(-1e30));
		
		 DebugDrawcallback
		 drawCallback(getDebugDrawer(),worldTransform,color);
		 concaveMesh.processAllTriangles(&drawCallback,aabbMin,aabbMax);
		
		 }
		
		 if (shape.getShapeType() == CONVEX_TRIANGLEMESH_SHAPE_PROXYTYPE)
		 {
		 btConvexTriangleMeshShape* convexMesh = (btConvexTriangleMeshShape*)
		 shape;
		 //todo: pass camera for some culling
		 Vector3f aabbMax(float(1e30),float(1e30),float(1e30));
		 Vector3f aabbMin(float(-1e30),float(-1e30),float(-1e30));
		 //DebugDrawcallback drawCallback;
		 DebugDrawcallback
		 drawCallback(getDebugDrawer(),worldTransform,color);
		 convexMesh.getMeshInterface().InternalProcessAllTriangles(&drawCallback,aabbMin,aabbMax);
		 }
		
		
		 /// for polyhedral shapes
		 if (shape.isPolyhedral())
		 {
		 btPolyhedralConvexShape* polyshape = (btPolyhedralConvexShape*)
		 shape;
		
		 int i;
		 for (i=0;i<polyshape.getNumEdges();i++)
		 {
		 btPoint3 a,b;
		 polyshape.getEdge(i,a,b);
		 Vector3f wa = worldTransform * a;
		 Vector3f wb = worldTransform * b;
		 getDebugDrawer().drawLine(wa,wb,color);
		
		 }
		
		
		 }
		 }
		 }
		 }
	}
}
