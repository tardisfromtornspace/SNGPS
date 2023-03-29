package LaPrac;

public class VecUtil<T>
{
	private double precision = 1e-6d;
	public static class Vec2<T>
	{
		private T x, y;
		Vec2(T x, T y)
		{
			this.x = x;
			this.y = y;
		}
		Vec2(Vec2<T> cpy)
		{
			this.x = cpy.getX();
			this.y = cpy.getY();
		}
		public void set(T x, T y)
		{
			this.x = x;
			this.y = y;
		}
		public void setX(T x)
		{
			this.x = x;
		}
		public T getX()
		{
			return this.x;
		}
		public void setY(T y)
		{
			this.y = y;
		}
		public T getY()
		{
			return this.y;
		}
		public String toString()
		{
			return "["+ x +","+ y +"]";
		}
	}
	
	public VecUtil()
	{
	}
	
	public VecUtil(double precision)
	{
		this.precision = precision;
	}
	public void setPrecission(double precision)
	{
		this.precision = precision;
	}
	
	// Set operations
	
	// Scalar operations
	
	public void vec2Adds(Vec2<Double> v, Double s)
	{
		v.set(
				v.getX() + s,
				v.getY() + s
				);
	}
	public void vec2Muls(Vec2<Double> v, Double s)
	{
		v.set(
				v.getX() * s,
				v.getY() * s
				);
	}
	
	// Vectorial operations
	public void vec2Addv(Vec2<Double> v, Vec2<Double> w)
	{
		v.set(
				v.getX() + w.getX(),
				v.getY() + w.getY()
				);
	}
	public void vec2Subv(Vec2<Double> v, Vec2<Double> w)
	{
		v.set(
				v.getX() - w.getX(),
				v.getY() - w.getY()
				);
	}
	public void vec2Mulv(Vec2<Double> v, Vec2<Double> w)
	{
		v.set(
				v.getX() * w.getX(),
				v.getY() * w.getY()
				);
	}
	public void vec2Divv(Vec2<Double> v, Vec2<Double> w)
	{
		v.set(
				(double)v.getX() / w.getX(),
				(double)v.getY() / w.getY()
				);
	}
	
	// Misc-operations
	
	public static Vec2<Double> getDefaultVec()
	{
		return new Vec2<Double>(0.d, 0.d);
	}
	
	public boolean nearAbsoluteZero(Vec2<Double> v)
	{
		return (
				((Math.abs(v.getX()) - this.precision) < 1e-10d) &&
				((Math.abs(v.getY()) - this.precision) < 1e-10d)
				);
	}
	
	// Get operations
	// Get scalar operations value
	public Vec2<Double> getVec2Adds(Vec2<Double> v, Double s)
	{
		if(v == null || s == null)
			return getDefaultVec();
	
		return new Vec2<Double>(
				v.getX() + s,
				v.getY() + s
				);
	}
	public Vec2<Double> getVec2Muls(Vec2<Double> v, Double s)
	{
		if(v == null || s == null)
			return getDefaultVec();
		
		return new Vec2<Double>(
				v.getX() * s,
				v.getY() * s
				);
	}
	// Get vectorial operations value
	public Vec2<Double> getVec2Addv(Vec2<Double> v, Vec2<Double> w)
	{
		if(v == null || w == null)
			return getDefaultVec();

		return new Vec2<Double>(
				v.getX() + w.getX(),
				v.getY() + w.getY()
				);
	}
	public Vec2<Double> getVec2Subv(Vec2<Double> v, Vec2<Double> w)
	{
		if(v == null || w == null)
			return getDefaultVec();

		return new Vec2<Double>(
				v.getX() - w.getX(),
				v.getY() - w.getY()
				);
	}
	public Vec2<Double> getVec2Mulv(Vec2<Double> v, Vec2<Double> w)
	{
		if(v == null || w == null)
			return getDefaultVec();

		return new Vec2<Double>(
				v.getX() * w.getX(),
				v.getY() * w.getY()
				);
	}
	public Vec2<Double> getVec2Divv(Vec2<Double> v, Vec2<Double> w)
	{
		if(v == null || w == null)
			return getDefaultVec();
		
		return new Vec2<Double>(
				(double)v.getX() / w.getX(),
				(double)v.getY() / w.getY()
				);
	}
	
	public double getVec2DMod(Vec2<T> v)
	{
		if(v == null)
			return 0.0d;
		double x = (double) v.getX();
		double y = (double) v.getY();
		return Math.sqrt(
				x * x +
				y * y
				);
	}
	public double getVec2DModv(Vec2<T> v, Vec2<T> w)
	{
		if(v == null || w == null)
			return 0.0d;
		double x = (double) v.getX();
		double y = (double) v.getY();
		double X = (double) w.getX();
		double Y = (double) w.getY();
		return Math.sqrt(
				(x-X) * (x-X) +
				(y-Y) * (y-Y)
				);
	}
}
