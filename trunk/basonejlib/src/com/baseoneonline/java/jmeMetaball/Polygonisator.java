package com.baseoneonline.java.jmeMetaball;

import gnu.trove.TFloatArrayList;
import gnu.trove.TIntArrayList;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import com.jme.math.Vector3f;
import com.jme.scene.TriMesh;
import com.jme.util.geom.BufferUtils;
 
/**
 * Based on Paul Bourke's code from "Polygonising a Scalar Field Using Tetrahedrons"
 * http://local.wasp.uwa.edu.au/~pbourke/geometry/polygonise/
 * 
 * @author Daniel Gronau
 */
public class Polygonisator {
 
    private final Vector3f boxSize;
    private final float cubeSize;
    private final float[][][] values;
    private final Coord[] cellCoords = new Coord[]{
        new Coord(), new Coord(), new Coord(), new Coord(),
        new Coord(), new Coord(), new Coord(), new Coord()
    };
    private final Vector3f[] cellPoints = new Vector3f[]{
        new Vector3f(), new Vector3f(), new Vector3f(), new Vector3f(),
        new Vector3f(), new Vector3f(), new Vector3f(), new Vector3f()
    };
    private final int[] cellIso = new int[8];
    private final TIntArrayList triangles = new TIntArrayList(5000);
    private int temp1;
    private int temp2;
    private final Map<Edge, Integer> interpol = new HashMap<Edge, Integer>(5000);
    private final TFloatArrayList vertexList = new TFloatArrayList(5000);
    private final TFloatArrayList normalList = new TFloatArrayList(5000);
    private int xk;
    private int yk;
    private int zk;
    private final int xSize;
    private final int ySize;
    private final int zSize;
    private ScalarField field;
    
    public Polygonisator(Vector3f boxSize, float cubeSize) {
        this.boxSize = boxSize;
        this.cubeSize = cubeSize;
        xSize = (int) Math.ceil(2 * boxSize.x / cubeSize);
        ySize = (int) Math.ceil(2 * boxSize.y / cubeSize);
        zSize = (int) Math.ceil(2 * boxSize.z / cubeSize);
        values = new float[xSize+1][ySize+1][zSize+1];
    }
 
    public void calculate(TriMesh mesh, ScalarField field, float iso) {
        this.field = field;
        triangles.clear();
        interpol.clear();
        vertexList.clear();
        normalList.clear();
 
        Vector3f vector = new Vector3f();
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                for (int z = 0; z < zSize; z++) {
                    vector.set(x * cubeSize - boxSize.x,
                            y * cubeSize - boxSize.y,
                            z * cubeSize - boxSize.z);
                    values[x][y][z] = field.calculate(vector);
                }
            }
        }
 
        for (xk = 0; xk < xSize; xk++) {
            for (yk = 0; yk < ySize; yk++) {
                for (zk = 0; zk < zSize; zk++) {
                    calculateCell(iso);
                }
            }
        }
 
        if (triangles.isEmpty()) {
            return;
        }
 

        mesh.setIndexBuffer(BufferUtils.createIntBuffer(triangles.toNativeArray()));
 
        mesh.setVertexBuffer(BufferUtils.createVector3Buffer(vertexList.size()/3));
        mesh.setNormalBuffer(BufferUtils.createVector3Buffer(vertexList.size()/3));
        mesh.setVertexCount(vertexList.size()/3);
        //batch.getTextureBuffers().set(0, BufferUtils.createVector2Buffer(???));
 
        FloatBuffer vb = mesh.getVertexBuffer();
        FloatBuffer nb = mesh.getNormalBuffer();
        for (int i = 0; i < vertexList.size(); i++) {
            vb.put(vertexList.get(i));
            nb.put(normalList.get(i));
        }
 
    }
 
    private void calculateCell(float iso) {
        
        cellCoords[0].set(xk, yk, zk);
        cellCoords[1].set(xk + 1, yk, zk);
        cellCoords[2].set(xk + 1, yk, zk + 1);
        cellCoords[3].set(xk, yk, zk + 1);
        cellCoords[4].set(xk, yk + 1, zk);
        cellCoords[5].set(xk + 1, yk + 1, zk);
        cellCoords[6].set(xk + 1, yk + 1, zk + 1);
        cellCoords[7].set(xk, yk + 1, zk + 1);
        
        int sum = 0;
        
        sum += cellIso[0] = values[xk][yk][zk] > iso ? 1 : 0;
        sum += cellIso[1] = values[xk+1][yk][zk] > iso ? 1 : 0;
        sum += cellIso[2] = values[xk+1][yk][zk+1] > iso ? 1 : 0;
        sum += cellIso[3] = values[xk][yk][zk+1] > iso ? 1 : 0;
        sum += cellIso[4] = values[xk][yk+1][zk] > iso ? 1 : 0;
        sum += cellIso[5] = values[xk+1][yk+1][zk] > iso ? 1 : 0;
        sum += cellIso[6] = values[xk+1][yk+1][zk+1] > iso ? 1 : 0;
        sum += cellIso[7] = values[xk][yk+1][zk+1] > iso ? 1 : 0;
 
        if (sum == 0 || sum == 8) {
            return;
        }        
        
        float x0 = xk * cubeSize - boxSize.x;
        float y0 = yk * cubeSize - boxSize.y;
        float z0 = zk * cubeSize - boxSize.z;
 
        float x1 = x0 + cubeSize;
        float y1 = y0 + cubeSize;
        float z1 = z0 + cubeSize;
 
        cellPoints[0].set(x0, y0, z0);
        cellPoints[1].set(x1, y0, z0);
        cellPoints[2].set(x1, y0, z1);
        cellPoints[3].set(x0, y0, z1);
        cellPoints[4].set(x0, y1, z0);
        cellPoints[5].set(x1, y1, z0);
        cellPoints[6].set(x1, y1, z1);
        cellPoints[7].set(x0, y1, z1);
 
        calculateTetra(iso, 0, 4, 7, 6);
        calculateTetra(iso, 0, 4, 6, 5);
        calculateTetra(iso, 0, 2, 6, 3);
        calculateTetra(iso, 0, 1, 6, 2);
        calculateTetra(iso, 0, 3, 6, 7);
        calculateTetra(iso, 0, 1, 5, 6);
    }
    
        private void calculateTetra(float iso, int v0, int v1, int v2, int v3) {
        int triindex = cellIso[v0] + cellIso[v1]*2 + cellIso[v2]*4 + cellIso[v3]*8;
 
        /* Form the vertices of the triangles for each case */
        switch (triindex) {
            case 0x00:
            case 0x0F:
                break;
            case 0x0E:
                triangles.add(interpolate(iso, v0, v1));
                triangles.add(interpolate(iso, v0, v2));
                triangles.add(interpolate(iso, v0, v3));
                break;
            case 0x01:
                triangles.add(interpolate(iso, v0, v1));
                triangles.add(interpolate(iso, v0, v3));
                triangles.add(interpolate(iso, v0, v2));
                break;
            case 0x0D:
                triangles.add(interpolate(iso, v1, v0));
                triangles.add(interpolate(iso, v1, v3));
                triangles.add(interpolate(iso, v1, v2));
                break;
            case 0x02:
                triangles.add(interpolate(iso, v1, v0));
                triangles.add(interpolate(iso, v1, v2));
                triangles.add(interpolate(iso, v1, v3));
                break;
            case 0x0C:
                temp1 = interpolate(iso, v0, v2);
                temp2 = interpolate(iso, v1, v3);
                triangles.add(interpolate(iso, v0, v3));
                triangles.add(temp2);
                triangles.add(temp1);
                triangles.add(temp2);
                triangles.add(interpolate(iso, v1, v2));
                triangles.add(temp1);
                break;
            case 0x03:
                temp1 = interpolate(iso, v0, v2);
                temp2 = interpolate(iso, v1, v3);
                triangles.add(interpolate(iso, v0, v3));
                triangles.add(temp1);
                triangles.add(temp2);
                triangles.add(temp2);
                triangles.add(temp1);
                triangles.add(interpolate(iso, v1, v2));
                break;
            case 0x0B:
                triangles.add(interpolate(iso, v2, v0));
                triangles.add(interpolate(iso, v2, v1));
                triangles.add(interpolate(iso, v2, v3));
                break;
            case 0x04:
                triangles.add(interpolate(iso, v2, v0));
                triangles.add(interpolate(iso, v2, v3));
                triangles.add(interpolate(iso, v2, v1));
                break;
            case 0x0A:
                temp1 = interpolate(iso, v0, v1);
                temp2 = interpolate(iso, v2, v3);
                triangles.add(temp1);
                triangles.add(temp2);
                triangles.add(interpolate(iso, v0, v3));
                triangles.add(temp1);
                triangles.add(interpolate(iso, v1, v2));
                triangles.add(temp2);
                break;
            case 0x05:
                temp1 = interpolate(iso, v0, v1);
                temp2 = interpolate(iso, v2, v3);
                triangles.add(temp1);
                triangles.add(interpolate(iso, v0, v3));
                triangles.add(temp2);
                triangles.add(temp1);
                triangles.add(temp2);
                triangles.add(interpolate(iso, v1, v2));
                break;
            case 0x09:
                temp1 = interpolate(iso, v0, v1);
                temp2 = interpolate(iso, v2, v3);
                triangles.add(temp1);
                triangles.add(interpolate(iso, v1, v3));
                triangles.add(temp2);
                triangles.add(temp1);
                triangles.add(temp2);
                triangles.add(interpolate(iso, v0, v2));
                break;
            case 0x06:
                temp1 = interpolate(iso, v0, v1);
                temp2 = interpolate(iso, v2, v3);
                triangles.add(temp1);
                triangles.add(temp2);
                triangles.add(interpolate(iso, v1, v3));
                triangles.add(temp1);
                triangles.add(interpolate(iso, v0, v2));
                triangles.add(temp2);
                break;
            case 0x07:
                triangles.add(interpolate(iso, v3, v0));
                triangles.add(interpolate(iso, v3, v2));
                triangles.add(interpolate(iso, v3, v1));
                break;
            case 0x08:
                triangles.add(interpolate(iso, v3, v0));
                triangles.add(interpolate(iso, v3, v1));
                triangles.add(interpolate(iso, v3, v2));
                break;
        }
    }
 
 
    private int interpolate(float iso, int v1, int v2) {
        Edge e = new Edge(cellCoords[v1], cellCoords[v2]);
        if (interpol.containsKey(e)) {
            return interpol.get(e);
        }
        float ratio = 0.5f;
        float value1 = cellCoords[v1].getValue();
        float value2 = cellCoords[v2].getValue();
        if (value1 != value2) {
            ratio = (value1 - iso) / (value1 - value2);
        }
        Vector3f point = cellPoints[v1].mult(1 - ratio).add(cellPoints[v2].mult(ratio));
        int i = vertexList.size()/3;
        vertexList.add(point.x);
        vertexList.add(point.y);
        vertexList.add(point.z);
        Vector3f normal = field.normal(point);
        normalList.add(normal.x);
        normalList.add(normal.y);
        normalList.add(normal.z);
        interpol.put(e, i);
        return i;
    }
 
    private class Coord implements Comparable<Coord> {
 
        int x = 0;
        int y = 0;
        int z = 0;
 
        Coord() {
        }
 
        Coord(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
 
        public void set(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
 
        public float getValue() {
            return values[x][y][z];
        }
 
        @Override
        public boolean equals(Object o) {
            Coord c = (Coord) o;
            return x == c.x && y == c.y && z == c.z;
        }
 
        @Override
        public int hashCode() {
            int hash = 7;
            hash = 59 * hash + this.x;
            hash = 61 * hash + this.y;
            hash = 73 * hash + this.z;
            return hash;
        }
 
        public int compareTo(Coord c) {
            if (c.x != x) {
                return c.x - x;
            }
            if (c.y != y) {
                return c.y - y;
            }
            return c.z - z;
        }
    }
 
    private static class Edge {
 
        int x1, y1, z1, x2, y2, z2;
 
        public Edge(Coord c1, Coord c2) {
            if (c1.compareTo(c2) < 0) {
                x1 = c1.x;
                y1 = c1.y;
                z1 = c1.z;
                x2 = c2.x;
                y2 = c2.y;
                z2 = c2.z;
            } else {
                x1 = c2.x;
                y1 = c2.y;
                z1 = c2.z;
                x2 = c1.x;
                y2 = c1.y;
                z2 = c1.z;
            }
        }
 
        @Override
        public int hashCode() {
            int hash = 3;
            hash = 59 * hash + this.x1;
            hash = 61 * hash + this.y1;
            hash = 67 * hash + this.z1;
            hash = 71 * hash + this.x2;
            hash = 73 * hash + this.y2;
            hash = 79 * hash + this.z2;
            return hash;
        }
 
        @Override
        public boolean equals(Object o) {
            Edge e = (Edge) o;
            return x1 == e.x1 && y1 == e.y1 && z1 == e.z1 &&
                    x2 == e.x2 && y2 == e.y2 && z2 == e.z2;
        }
    }
}