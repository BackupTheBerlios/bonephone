
package examples.proxy;

class Debug {
	public static final boolean debug = false;
	
	public static void println(String s) {
		System.out.println(s);
	}

	public static void Assert(boolean flag, String message) {
	  if (!flag) {
		Debug.println(message);
		new Exception(message).printStackTrace();
		System.exit(0);
	  }
	}
}
