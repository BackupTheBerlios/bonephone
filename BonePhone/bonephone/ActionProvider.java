package bonephone;

/**
 * Interface used by the StateMachine class to execute previously defined
 * actions as a reaction to events. 
 *
 * @author Jens Fiedler (jfi@fokus.gmd.de)
*/
public interface ActionProvider {
	public boolean action (Object command);
}

