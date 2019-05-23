package net.pimathclanguage.rotationpuzzle;

import java.util.Arrays;

public class Tuple {
    public final static class Tuple2<V1, V2> {
        private V1 v1;
        private V2 v2;
        
        public Tuple2(V1 v1, V2 v2) {
            this.v1 = v1;
            this.v2 = v2;
        }
        
        public V1 getV1() { return this.v1; }
        public V2 getV2() { return this.v2; }
        public Object get(int index) {
            switch (index) {
                case 0:
                    return this.v1;
                case 1:
                    return this.v2;
            }
            return null;
        }
        
        public void setV1(V1 v1) { this.v1 = v1; }
        public void setV2(V2 v2) { this.v2 = v2; }
        public void set(int index, Object v) {
            switch (index) {
                case 0:
                    v1 = (V1)v;
                case 1:
                    v2 = (V2)v;
            }
        }

        @Override
        public String toString() {
            return "("+v1.toString()+", "+v2.toString()+")";
        }

        @Override
        public int hashCode() {
            return v1.hashCode() ^ v2.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            Tuple2<V1, V2> oc = (Tuple2<V1, V2>)o;
            return v1.equals(oc.v1) && v2.equals(oc.v2);
        }
    }

    public final static class Tuple3<V1, V2, V3> {
        private V1 v1;
        private V2 v2;
        private V3 v3;

        public Tuple3(V1 v1, V2 v2, V3 v3) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
        }

        public V1 getV1() { return this.v1; }
        public V2 getV2() { return this.v2; }
        public V3 getV3() { return this.v3; }
        public Object get(int index) {
            switch (index) {
                case 0:
                    return this.v1;
                case 1:
                    return this.v2;
                case 2:
                    return this.v3;
            }
            return null;
        }

        public void setV1(V1 v1) { this.v1 = v1; }
        public void setV2(V2 v2) { this.v2 = v2; }
        public void setV3(V3 v3) { this.v3 = v3; }
        public void set(int index, Object v) {
            switch (index) {
                case 0:
                    v1 = (V1)v;
                case 1:
                    v2 = (V2)v;
                case 2:
                    v3 = (V3)v;
            }
        }

        @Override
        public String toString() {
            return "("+v1.toString()+", "+v2.toString()+", "+v3.toString()+")";
        }

        @Override
        public int hashCode() {
            return v1.hashCode() ^ v2.hashCode() ^ v3.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            Tuple3<V1, V2, V3> oc = (Tuple3<V1, V2, V3>)o;
            return v1.equals(oc.v1) && v2.equals(oc.v2) && v3.equals(oc.v3);
        }
    }

    public final static class Tuple4<V1, V2, V3, V4> {
        public V1 v1;
        public V2 v2;
        public V3 v3;
        public V4 v4;

        public Tuple4(V1 v1, V2 v2, V3 v3, V4 v4) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.v4 = v4;
        }

        public V1 getV1() { return this.v1; }
        public V2 getV2() { return this.v2; }
        public V3 getV3() { return this.v3; }
        public V4 getV4() { return this.v4; }
        public Object get(int index) {
            switch (index) {
                case 0:
                    return this.v1;
                case 1:
                    return this.v2;
                case 2:
                    return this.v3;
                case 3:
                    return this.v4;
            }
            return null;
        }

        public void setV1(V1 v1) { this.v1 = v1; }
        public void setV2(V2 v2) { this.v2 = v2; }
        public void setV3(V3 v3) { this.v3 = v3; }
        public void setV4(V4 v4) { this.v4 = v4; }
        public void set(int index, Object v) {
            switch (index) {
                case 0:
                    this.v1 = (V1)v;
                    break;
                case 1:
                    this.v2 = (V2)v;
                    break;
                case 2:
                    this.v3 = (V3)v;
                    break;
                case 3:
                    this.v4 = (V4)v;
                    break;
            }
        }

        @Override
        public String toString() {
            return "("+v1.toString()+", "+v2.toString()+", "+v3.toString()+", "+v4.toString()+")";
        }

        @Override
        public int hashCode() {
            return v1.hashCode() ^ v2.hashCode() ^ v3.hashCode() ^ v4.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            Tuple4<V1, V2, V3, V4> oc = (Tuple4<V1, V2, V3, V4>)o;
            return v1.equals(oc.v1) && v2.equals(oc.v2) && v3.equals(oc.v3) && v4.equals(oc.v4);
        }
    }

    public class TupleMany<T> {
        private final T[] contents;

        public TupleMany (T[] contents) {
            if (contents.length != 2)
                throw new IllegalArgumentException();
            this.contents = contents;
        }

        public T[] getContents () {
            return this.contents.clone();
        }

        @Override
        public int hashCode () {
            return Arrays.deepHashCode(this.contents);
        }

        @Override
        public boolean equals (Object other) {
            return Arrays.deepEquals(this.contents, ((TupleMany<T>)other).getContents());
        }

        @Override
        public String toString () {
            return Arrays.deepToString(this.contents);
        }
    }
}
