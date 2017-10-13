/**
 * Representation of a graph vertex
 */
public class Vertex implements Comparable<Vertex> {
        private final String label;   // label attached to this vertex
        private int cost;
        private boolean known;
        private Vertex path;

        /**
         * Construct a new vertex
         * @param label the label attached to this vertex
         */
        public Vertex(String label) {
                if(label == null)
                        throw new IllegalArgumentException("null");
                this.label = label;
                this.cost = Integer.MAX_VALUE; 
                this.known = false;
                this.path = null;
        }

        /**
         * Get a vertex label
         * @return the label attached to this vertex
         */
        public String getLabel() {
                return label;
        }
        
        /**
         * A string representation of this object
         * @return the label attached to this vertex
         */
        @Override
        public String toString() {
                return label;
        }

        //auto-generated: hashes on label
        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((label == null) ? 0 : label.hashCode());
                return result;
        }

        //auto-generated: compares labels
        @Override
        public boolean equals(Object obj) {
                if (this == obj)
                        return true;
                if (obj == null)
                        return false;
                if (getClass() != obj.getClass())
                        return false;
                final Vertex other = (Vertex) obj;
                if (label == null) {
                    return other.label == null;
                } else {
                    return label.equals(other.label);
                }
        }

        // Returns an integer value after comparing cost of two vertices
        @Override
        public int compareTo(Vertex other) {
            return Integer.compare(this.cost, other.cost);
        }

        // Returns the cost of the vertex
        public int getCost() {
            return cost;
        }
        
        // Returns true if the vertex is known and false otherwise
        public boolean getKnown() {
        	return known;
        }
        
        // Returns the vertex in the path
        public Vertex getPath() {
        	return path;
        }
        
        // Update cost of the vertex
        public void setCost(int cost) {
        	this.cost = cost;
        }
        
        // Update known to the given boolean statement
        public void setKnown(boolean known) {
        	this.known = known;
        }
        
        // Update path to new path of the vertex
        public void setPath(Vertex current) {
        	this.path = current;
        }
}