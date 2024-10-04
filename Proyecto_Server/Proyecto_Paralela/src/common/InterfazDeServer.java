package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

public interface InterfazDeServer extends Remote {
	ArrayList<Persona> getPersonas() throws RemoteException;
	JsonNode getListaAnimales() throws RemoteException;
	void guardaUsuarioBd(Persona persona) throws RemoteException;
	void actualizarPersona(Persona persona) throws RemoteException;
	void borrarPersona(Persona persona) throws RemoteException;
	
	// MUTEX
	boolean requestMutex() throws RemoteException;
	void releaseMutex() throws RemoteException;
}
