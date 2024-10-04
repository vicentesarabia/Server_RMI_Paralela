package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import common.InterfazDeServer;
import common.Persona;

public class ServerImpl implements InterfazDeServer{
	
	private boolean inUse;

	public ServerImpl() throws RemoteException {
		conectarBD();
		UnicastRemoteObject.exportObject(this, 0);
	}
	
	//crear arreglo para respaldo de bd
	private ArrayList<Persona> bd_copia = new ArrayList<>();
	
	public void conectarBD() {
		Connection connection = null;
		Statement query = null;
		//PreparedStatement test = null;
		ResultSet resultados = null;
		
		
		try {
			String url = "jdbc:mysql://localhost:3307/mydb";
			String username = "root";
			String password_BD = "";
			
			connection = DriverManager.getConnection(url, username, password_BD);
			
			//TODO Metodos con la BD
			//TODO agregar mutex
			
			
			
			query = connection.createStatement();
			String sql = "SELECT * FROM usuario";
			//INSERT para agregar datos a la BD, PreparedStatement
			//insert into usuario (comuna,direccion,nombre,apellido,idMascota) values('Valparaíso','Calle del agua','Benjamin','Pino','1')
			resultados = query.executeQuery(sql);
			
			while (resultados.next()) {
				int idUsuario = resultados.getInt("idUsuario");
				String comuna = resultados.getString("comuna");
				String direccion = resultados.getString("direccion");
				String nombre = resultados.getString("nombre");
				String apellido = resultados.getString("apellido");
				int idMascota = resultados.getInt("idMascota");
				
				Persona newPersona = new Persona(idUsuario,comuna, direccion, nombre, apellido,idMascota);
				
				bd_copia.add(newPersona);
				
			}
			connection.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("No se pudo conectar a la BD");
		}
	}
	
	@Override
	public ArrayList<Persona> getPersonas() throws RemoteException {
		// TODO Auto-generated method stub
		return bd_copia;
	}

	@Override
	public JsonNode getListaAnimales() throws RemoteException {
		String output = null;
		 
		try {
            // URL de la API REST, el listado de APIs públicas está en: 
			// https://github.com/juanbrujo/listado-apis-publicas-en-chile
            URL apiUrl = new URL("https://huachitos.cl/api/animales");
            
            // Abre la conexión HTTP
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            // Configura la solicitud (método GET en este ejemplo)
            connection.setRequestMethod("GET");

            // Obtiene el código de respuesta
            int responseCode = connection.getResponseCode();

            // Procesa la respuesta si es exitosa
            if (responseCode == HttpURLConnection.HTTP_OK) {
            	
                // Lee la respuesta del servidor
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
            

                while ((inputLine = in.readLine()) != null) {
                	
                    response.append(inputLine);
                }

                // Cierra la conexión y muestra la respuesta
                in.close();
                output = response.toString();
              
            } else {
                System.out.println("Error al conectar a la API. Código de respuesta: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		//Como resultado tenemos un String output que contiene el JSON de la respuesta de la API
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			JsonNode jsonNode = objectMapper.readTree(output);
			JsonNode dataArray = jsonNode.path("data");	
			return dataArray;
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	// INSERT
	@Override
	public void guardaUsuarioBd(Persona usuario) throws RemoteException
	{
		Connection connection = null;
		Statement query = null;
		//PreparedStatement test = null;
		int resultados;
		
		while(true) {
			if(requestMutex()) {
				System.out.println("Permiso para iniciar la sección crítica");
				break;
			}
			try {
				Thread.sleep(2000);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Aun no hay permiso para iniciar la sección crítica");
		}
		
		try {
			String url = "jdbc:mysql://localhost:3307/mydb";
			String username = "root";
			String password_BD = "";
			
			connection = DriverManager.getConnection(url, username, password_BD);
			
			//TODO Metodos con la BD
			query = connection.createStatement();
			String sql = "insert into usuario (comuna,direccion,nombre,apellido,idMascota) values('" + usuario.getComuna() +"','" +usuario.getDireccion() +"','" +usuario.getNombre()+"','" +usuario.getApellido() +"','"+usuario.getIdMascota()+"')";
			int idcopy;
			if (!bd_copia.isEmpty()) {
	            Persona ultimoElemento = bd_copia.get(bd_copia.size() - 1);
	            idcopy=ultimoElemento.getIdUsuario();
	            usuario.setIdUsuario(idcopy+1);
	        } else {
	            System.out.println("La lista está vacía.");
	        }
			
			bd_copia.add(usuario);
			//INSERT para agregar datos a la BD, PreparedStatement
			resultados = query.executeUpdate(sql);
			connection.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("No se pudo conectar a la BD");
		}
		
		releaseMutex();
	}
	
	
	public synchronized boolean requestMutex() throws RemoteException {
		if(inUse) {
			return false;
		} else {
			inUse = true;
			return true;
		}
	}
	
	public synchronized void releaseMutex() throws RemoteException {
		inUse = false;
	}

	@Override
	public void actualizarPersona(Persona usuario) throws RemoteException {
		// TODO Auto-generated method stub
		Connection connection = null;
		Statement query = null;
		//PreparedStatement test = null;
		int resultados;
		
		while(true) {
			if(requestMutex()) {
				System.out.println("Permiso para iniciar la sección crítica");
				break;
			}
			try {
				Thread.sleep(2000);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Aun no hay permiso para iniciar la sección crítica");
		}
		
		try {
			String url = "jdbc:mysql://localhost:3307/mydb";
			String username = "root";
			String password_BD = "";
			
			connection = DriverManager.getConnection(url, username, password_BD);
			
			//TODO Metodos con la BD
			query = connection.createStatement();
			String sql = "UPDATE usuario SET comuna = '" + usuario.getComuna() +"',direccion = '"+ usuario.getDireccion() +"',nombre = '" +usuario.getNombre()+ "',apellido = '" +usuario.getApellido() +"',idMascota = '"+ usuario.getIdMascota()+ "' WHERE idUsuario ="+ usuario.getIdUsuario()+";";

			for(int i = 1;i<bd_copia.size();i++)
			{
				Persona persona = bd_copia.get(i);
				if(usuario.getIdUsuario()==persona.getIdUsuario())
				{
					bd_copia.set(i, usuario);
				}
				
			}
			
			//Update para agregar datos a la BD, PreparedStatement
			resultados = query.executeUpdate(sql);
			connection.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("No se pudo conectar a la BD");
		}
		
		releaseMutex();
		
		
	}

	@Override
	public void borrarPersona(Persona usuario) throws RemoteException {
	
		// TODO Auto-generated method stub
				Connection connection = null;
				Statement query = null;
				//PreparedStatement test = null;
				int resultados;
				
				while(true) {
					if(requestMutex()) {
						System.out.println("Permiso para iniciar la sección crítica");
						break;
					}
					try {
						Thread.sleep(2000);
					} catch(InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("Aun no hay permiso para iniciar la sección crítica");
				}
				
				try {
					String url = "jdbc:mysql://localhost:3307/mydb";
					String username = "root";
					String password_BD = "";
					
					connection = DriverManager.getConnection(url, username, password_BD);
					
					//TODO Metodos con la BD
					query = connection.createStatement();
					//String sql1 = "UPDATE usuario SET comuna = '" + usuario.getComuna() +"',direccion = '"+ usuario.getDireccion() +"',nombre = '" +usuario.getNombre()+ "',apellido = '" +usuario.getApellido() +"',idMascota = '"+ usuario.getIdMascota()+ "' WHERE idUsuario ="+ usuario.getIdUsuario()+";";
					String sql = "DELETE FROM usuario WHERE idUsuario ="+usuario.getIdUsuario()+";";
					for(int i = 1;i<bd_copia.size();i++)
					{
						Persona persona = bd_copia.get(i);
						if(usuario.getIdUsuario()==persona.getIdUsuario())
						{
							bd_copia.remove(i);
						}
						
					}
					
					//Update para agregar datos a la BD, PreparedStatement
					resultados = query.executeUpdate(sql);
					connection.close();
					
				} catch(SQLException e) {
					e.printStackTrace();
					System.out.println("No se pudo conectar a la BD");
				}
				
				releaseMutex();
				
				
			}
		
	
	
}
