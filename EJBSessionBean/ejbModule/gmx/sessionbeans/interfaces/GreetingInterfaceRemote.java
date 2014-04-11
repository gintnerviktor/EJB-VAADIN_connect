package gmx.sessionbeans.interfaces;

import javax.ejb.Remote;

@Remote
public interface GreetingInterfaceRemote {
	public String sayHello( String nev);
}
