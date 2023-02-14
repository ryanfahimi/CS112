class MyInt {
	Integer m_value;
	
	// Constructors
	MyInt(int x) { m_value = x; }
	MyInt(Integer x) { m_value = x; }
	MyInt(MyInt original) { m_value = original.m_value; }
	
	// Member functions
	Integer DoubleMe() { m_value *= 2; return m_value; }
	Integer DoubleMe(MyInt arg) {
		m_value = arg.DoubleMe();
		return m_value;
	}
	void set(Integer i) { m_value = i; }

	// Return a String that shows the value of this MyInt.
	public String toString() { return m_value.toString(); }
}


public class ObjsAndRefs {
	// A function to double and return an Integer value.
	static Integer DoubleMe(Integer arg) {
		arg *= 2;
		return arg;
	}

	static void FunWithInteger() {
		Integer x = 1;
		Integer y = x;

		DoubleMe(x);
		System.out.println("A: " + x + ", " + y);
		y = DoubleMe(x);
		System.out.println("B: " + x + ", " + y);
		x = DoubleMe(y);
		System.out.println("C: " + x + ", " + y);
		x = DoubleMe(x);
		System.out.println("D: " + x + ", " + y);
	}

	static void FunWithMyInt() {
		MyInt x = new MyInt(1);
		MyInt y = new MyInt(x);

		x.DoubleMe();
		System.out.println("E: " + x + ", " + y);
		
		y.set(x.DoubleMe());
		System.out.println("F: " + x + ", " + y);
		
		y.DoubleMe(x);
		System.out.println("G: " + x + ", " + y);
		
		x.set(x.DoubleMe(x));
		System.out.println("H: " + x + ", " + y);

		y.DoubleMe();
		System.out.println("I: " + x + ", " + y);
	}

	static public void main(String[] args) {
		FunWithInteger();
		FunWithMyInt();
	}
}