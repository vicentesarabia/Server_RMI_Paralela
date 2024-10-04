package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.text.Normalizer;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;

import common.InterfazDeServer;
import common.Persona;

public class Cliente {
	private InterfazDeServer server;
	
	public Cliente() {	}
	
	public void cambiarAServerRespaldo() {
		
	}
	
	public void startCliente(String servidor_seleccionado) throws RemoteException, NotBoundException {
		if(servidor_seleccionado == "main") {
			Registry registry = LocateRegistry.getRegistry("localhost", 5000);
			server = (InterfazDeServer) registry.lookup("server");
			System.out.println("Cliente conectado al servidor principal");
		} else if(servidor_seleccionado == "backup") {
			Registry registry = LocateRegistry.getRegistry("localhost", 5001);
			server = (InterfazDeServer) registry.lookup("server");
			System.out.println("Cliente conectado al servidor de respaldo");
		} else {
			System.out.println("No se seleccionó el servidor");
		}
	}
	
	public ArrayList<Persona> getPersonas() throws RemoteException{
		ArrayList<Persona> listado_Adopcion = server.getPersonas();
		
		System.out.println("ID NOMBRE APELLIDO COMUNA DIRECCION IDCACHORRO");
		
		for (int i = 0; i < listado_Adopcion.size(); i++) {
			Persona usuario = listado_Adopcion.get(i);
			String nombre, apellido, comuna,direccion;
			int  idMascota, id;
			id=usuario.getIdUsuario();
			nombre = usuario.getNombre();
			apellido = usuario.getApellido();
			comuna = usuario.getComuna();
			direccion = usuario.getDireccion();
			idMascota = usuario.getIdMascota();
			
			System.out.println( id + " "+nombre + " "+ apellido + " " + comuna + " " + direccion+ " " + idMascota);
		}
		
		return null;
	}
	
	
	
	JsonNode getListaAnimales() throws RemoteException {
		JsonNode Lista_Cachorrines = server.getListaAnimales();
		
		if(Lista_Cachorrines == null) {
			System.out.println("Hubo un error, no llego nada de la API");
			return null;
		} else {
			//String nombre = (String) valores_de_uf[1];
			
			
		}
		
		return Lista_Cachorrines;
	}
	void buscarComuna() throws RemoteException  , IOException  
	{
		System.out.println("Ingrese la Comuna: ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String Comuna  = br.readLine();
		Comuna = quitaDiacriticos(Comuna);
		
		JsonNode Lista_Cachorrines = this.getListaAnimales();
		
		for (JsonNode item : Lista_Cachorrines) {
			String comuna = item.get("comuna").asText();
			comuna=quitaDiacriticos(comuna);
			
			if(Comuna.replace(" ", "").toUpperCase().equals(comuna.replace(" ", "").toUpperCase()))
			{
				String nombre = item.get("nombre").asText();
				String id = item.get("id").asText();
				String animal = item.get("tipo").asText();
			    System.out.println("id: " + id + " nombre: " + nombre  +" Comuna: " + comuna + " Animal: "+ animal );
			}
		     
		     
		 }
	}
	void buscarEdad() throws RemoteException  , IOException  
	{
		System.out.println("Ingrese la Edad maxima: ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int edadMax  = Integer.parseInt(br.readLine());
		JsonNode Lista_Cachorrines = this.getListaAnimales();
		
		for (JsonNode item : Lista_Cachorrines) {
			int edad;
			String edadStr = item.get("edad").asText();
			if (edadStr.contains("Meses")) {
                edad = Integer.parseInt(edadStr.split(" ")[0]) / 12;
            } 
            else if(edadStr.contains("Adulto"))
			{
				edad = 3;
			}
            else if(edadStr.contains("Cachorro"))
			{
				edad = 1;
			}
            else {
                edad = Integer.parseInt(edadStr.split(" ")[0]); // Edad en años
            }

            if (edad <= edadMax) {
                String nombre = item.get("nombre").asText().replace("\u00f1", "ny");
                String id = item.get("id").asText();
                String animal = item.get("tipo").asText();
                System.out.println("id: " + id + " nombre: " + nombre + " Edad: " + edad + " Animal: " + animal);
            }
			
		}
			
	}
	void buscarAnimal() throws RemoteException  , IOException  
	{
		System.out.println("Ingrese el tipo de animal: ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String Animal  = br.readLine();
		JsonNode Lista_Cachorrines = this.getListaAnimales();
		
		for (JsonNode item : Lista_Cachorrines) {
			String animal = item.get("tipo").asText();
			if(Animal.replace(" ", "").toUpperCase().equals(animal.replace(" ", "").toUpperCase()))
			{
				String nombre = item.get("nombre").asText();
				String id = item.get("id").asText();
				String comuna = item.get("comuna").asText();
			
				
			    System.out.println("id: " + id + " nombre: " + nombre  +" Comuna: " + comuna + " Animal: "+ animal );
			}
		     
		     
		 }
	}
	public void modificarAdoptador() throws RemoteException, IOException {
	    ArrayList<Persona> listado_Adopcion = server.getPersonas();
	    System.out.println("Ingrese ID adoptante: ");
	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    System.in.skip(System.in.available());
	 
	    String idAdoptante = br.readLine();
	    
	    int idint = Integer.parseInt(idAdoptante);

	    Persona usuarioEncontrado = null;
	    for (Persona usuario : listado_Adopcion) {
	        if (usuario.getIdUsuario() == idint) {
	            usuarioEncontrado = usuario;
	            break;
	        }
	    }
	  

	    if (usuarioEncontrado != null) {
	        System.out.println(usuarioEncontrado.getNombre() + " " + usuarioEncontrado.getApellido() + " " +
	                usuarioEncontrado.getComuna() + " " + usuarioEncontrado.getDireccion() +" id mascota:"+ usuarioEncontrado.getIdMascota()+ " id: " + usuarioEncontrado.getIdUsuario());
	        System.out.println("Ingrese nuevo nombre: ");
	        String nuevoNombre = br.readLine();
	        System.out.println("Ingrese nuevo apellido: ");
	        String nuevoApellido = br.readLine();
	        System.out.println("Ingrese nueva comuna: ");
	        String nuevaComuna = br.readLine();
	        System.out.println("Ingrese nueva direccion: ");
	        String nuevaDireccion = br.readLine();
	        System.out.println("Ingrese nuevo id mascota: ");
	        String mascotaNueva = br.readLine();

	        usuarioEncontrado.setNombre(nuevoNombre);
	        usuarioEncontrado.setApellido(nuevoApellido);
	        usuarioEncontrado.setComuna(nuevaComuna);
	        usuarioEncontrado.setDireccion(nuevaDireccion);
	        usuarioEncontrado.setIdMascota(Integer.parseInt(mascotaNueva));
	        

	        server.actualizarPersona(usuarioEncontrado);
	        System.out.println("Datos del adoptante actualizados.");
	    } else {
	        System.out.println("Adoptante con ID " + idint + " no encontrado.");
	    }
	   
	}
	public static String quitaDiacriticos(String s) {
	    s = Normalizer.normalize(s, Normalizer.Form.NFD);
	    s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
	    return s;
	}
	void GuardarUsuarioAdopcion() throws RemoteException, IOException 
	{
		Persona usuario = new Persona();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.in.skip(System.in.available());
		System.out.println("Ingrese su nombre: ");
		String nombre = br.readLine();
		usuario.setNombre(nombre);
		

		System.out.println("Ingrese su apellido: ");
		String apellido = br.readLine();
		usuario.setApellido(apellido);

		System.out.println("Ingrese su comuna: ");
		String comuna = br.readLine();
		usuario.setComuna(comuna);

		System.out.println("Ingrese su direccion: ");
		String direccion = br.readLine();
		usuario.setDireccion(direccion);

		System.out.println("Ingrese el id de la mascota: ");
		String idMascota = br.readLine();
		usuario.setIdMascota(Integer.parseInt(idMascota));
		System.in.skip(System.in.available());
		server.guardaUsuarioBd(usuario);	
	}
	void borrarUsuarioAdop()throws RemoteException, IOException 
	{
		ArrayList<Persona> listado_Adopcion = server.getPersonas();
	    System.out.println("Ingrese ID adoptante: ");
	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    System.in.skip(System.in.available());
	 
	    String idAdoptante = br.readLine();
	    
	    int idint = Integer.parseInt(idAdoptante);

	    Persona usuarioEncontrado = null;
	    for (Persona usuario : listado_Adopcion) {
	        if (usuario.getIdUsuario() == idint) {
	            usuarioEncontrado = usuario;
	            break;
	        }
	    }
	    if(usuarioEncontrado != null)
	    {
	    	server.borrarPersona(usuarioEncontrado);
	    	System.out.println("Adoptante borrado con exito");
	   
	    } else {
	        System.out.println("Adoptante con ID " + idint + " no encontrado.");
	    }
	    
		
	}
}
