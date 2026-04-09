package com.pao.laboratory07.exercise1;

public enum StareComanda {
    PLACED {
        @Override
        public StareComanda next() {
            return PROCESSED;
        }
    },
    PROCESSED {
        @Override
        public StareComanda next() {
            return SHIPPED;
        }
    },
    SHIPPED {
        @Override
        public StareComanda next() {
            return DELIVERED;
        }
    },
    DELIVERED {
        @Override
        public StareComanda next() {
            return DELIVERED;
        }

        @Override
        public boolean esteFinala() {
            return true;
        }
    },
    CANCELED {
        @Override
        public StareComanda next() {
            return CANCELED;
        }

        @Override
        public boolean esteFinala() {
            return true;
        }
    };

    public abstract StareComanda next();

    public boolean esteFinala() {
        return false;
    }
}