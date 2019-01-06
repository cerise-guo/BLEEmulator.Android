package com.blesim.bluetooth;

/**
 * Created by cerise on 6/8/18.
 */

public class GLog {

    private static int STACK_TRACE_LEVELS_UP = 4;

    public static void d( String TAG, String message )
    {
        final String logMsg =  getFileName() + "::" + getMethodName()+" @ "+getLineNumber() + " : " + message;
        android.util.Log.d(TAG, logMsg );
    }

    public static void d()
    {
        final String logMsg =  getMethodName()+" @ "+getLineNumber();
        android.util.Log.d(getFileName(), logMsg );
    }

    public static void d(String message)
    {
        final String logMsg =  getMethodName()+" @ "+getLineNumber() + " : " + message;
        android.util.Log.d(getFileName(), logMsg );
    }

    public static void e( String TAG, String message )
    {
        final String logMsg =  getFileName()+ "::" +getMethodName()+" @ "+getLineNumber() + " : " + message;
        android.util.Log.e(TAG, logMsg );
    }

    public static void e(String message )
    {
        final String logMsg =  getMethodName()+" @ "+getLineNumber() + " : " + message;
        android.util.Log.e(getFileName(), logMsg );
    }

    public static void detectStackDepth(){

        //the 2 stack layer higher will make the stack point to caller's function instead of
        //this GLog class's function.
        final int THIS_STACK_DEPTH = 2;

        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        for( int i = 0; i< stacks.length; ++i ){
            if( stacks[i].getMethodName().equals("detectStackDepth")){
                STACK_TRACE_LEVELS_UP = i+THIS_STACK_DEPTH;
                return;
            }
        }
        android.util.Log.d("LoggerBase","Failed to detect stack depth");
        if (!false) {
            throw new AssertionError();
        }
    }

    //Note: during test, run a loop with 10000 log.d() without pause,
    //the log with line number + FileName + methodName won't impact performance very much.
    //The performance difference is about 2% in above extreme condition.
    private static int getLineNumber()
    {
        return Thread.currentThread().getStackTrace()[STACK_TRACE_LEVELS_UP].getLineNumber();
    }

    private static String getFileName()
    {
        return Thread.currentThread().getStackTrace()[STACK_TRACE_LEVELS_UP].getFileName();
    }

    private static String getMethodName()
    {
        return Thread.currentThread().getStackTrace()[STACK_TRACE_LEVELS_UP].getMethodName();
    }
}
