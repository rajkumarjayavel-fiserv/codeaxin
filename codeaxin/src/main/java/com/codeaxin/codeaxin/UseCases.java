package com.codeaxin.codeaxin;

public enum UseCases {
    TRY_RESOURCES {
        @Override
        void execute() {
            System.out.print("Executing strategy A");
        }
    };
    abstract void execute();
}
