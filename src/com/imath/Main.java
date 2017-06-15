package com.imath;

import com.imath.program.Program;

public class Main {

    public static void main(String[] args) {
        Program program = new Program(true, true);
        Thread programThread = new Thread(program::initialize);
        programThread.start();
    }
}
