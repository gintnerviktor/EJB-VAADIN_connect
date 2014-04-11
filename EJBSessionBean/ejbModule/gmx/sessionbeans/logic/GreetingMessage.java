package gmx.sessionbeans.logic;

import gmx.sessionbeans.interfaces.GreetingInterfaceRemote;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * Session Bean implementation class GreetingInterface
 */
@Stateless
@LocalBean
public class GreetingMessage implements GreetingInterfaceRemote {

    /**
     * Default constructor. 
     */
    public GreetingMessage() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Egy egyszerű String átalakítás.
     */
	@Override
	public String sayHello(String nev) {
		return "Helló " + nev + " ! Üdvözöllek a Greeting Message Bean-ből !";
	}

}
