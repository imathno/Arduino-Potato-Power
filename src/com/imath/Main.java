package com.imath;

import com.imath.program.Program;

public class Main {

    public static void main(String[] args) {
        Program program = new Program(false, true);
        Thread programThread = new Thread(program::initialize);
        programThread.start();
    }

}
