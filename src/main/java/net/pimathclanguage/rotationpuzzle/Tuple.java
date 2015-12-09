package net.pimathclanguage.rotationpuzzle;
 
public class Tuple {
    public class Tuple2<V1, V2> {
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
    }
    
    public class Tuple3<V1, V2, V3> {
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
        public void setV3(V2 v2) { this.v2 = v2; }
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
    }
}
