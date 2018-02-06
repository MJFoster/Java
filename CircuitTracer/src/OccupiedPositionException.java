/**
 * Exception used for CircuitBoard tracing.
 * Thrown if current Point is unavailable as a TraceState.
 * 
 * @author MaryJo Foster
 * @version Summer 2016
 */
public class OccupiedPositionException extends RuntimeException
{

	private static final long serialVersionUID = 1411366337177233337L;

	public OccupiedPositionException(String msg)
	{
		super(msg);
	}
}
