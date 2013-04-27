package org.pasut.pruebas.neo4j.example;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	GraphDatabaseService db = openDatabase();
    	Transaction tx = db.beginTx();
    	Node node1 = db.createNode();
    	node1.setProperty("name", "nodo1");
    	Node node2 = db.createNode();
    	node2.setProperty("name", "nodo2");
    	
    	Relationship relation = node1.createRelationshipTo(node2, Rel.KNOW);
    	relation.setProperty("name", relation.getStartNode().getProperty("name") + " know " + relation.getEndNode().getProperty("name"));
    	tx.success();

    	System.out.println( "Hello World!" );
    }
    
    public static GraphDatabaseService openDatabase() {
//    	To start Neo4j with configuration settings, a Neo4j properties file can be loaded like this:
//
//    		GraphDatabaseService graphDb = new GraphDatabaseFactory()
//    		    .newEmbeddedDatabaseBuilder( "target/database/location" )
//    		    .loadPropertiesFromFile( pathToConfig + "neo4j.properties" )
//    		    .newGraphDatabase();
    	GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase( "/neo4j/neofj.db" );
    	registerShutdownHook( db );
    	return db;
    }
    
    public static void registerShutdownHook(final GraphDatabaseService db) {
    	// Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                db.shutdown();
            }
        } );
    }
}
