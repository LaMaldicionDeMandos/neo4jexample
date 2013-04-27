package org.pasut.pruebas.neo4j.example;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.TestGraphDatabaseFactory;

public class Neo4jTest {
	private GraphDatabaseService db;
	
	@Before
	public void before() {
		db = new TestGraphDatabaseFactory().newImpermanentDatabase();
	}
	
	@After
	public void after() {
		db.shutdown();
	}
	
	@Test
	public void create() {
		
		Node n = createTestNode("Aída");
		// The node should have an id greater than 0, which is the id of the
		// reference node.
		assertThat( n.getId(), is( greaterThan( 0l ) ) );
		 
		// Retrieve a node by using the id of the created node. The id's and
		// property should match.
		Node foundNode = db.getNodeById( n.getId() );
		assertThat( foundNode.getId(), is( n.getId() ) );
		assertThat( (String) foundNode.getProperty( "name" ), is( "Aída" ) );
	}
	
	@Test
	public void find() {
		Node n = createTestNode("Aída");
		Node n2 = createTestNode("Marcelo");
		Transaction tx = db.beginTx();
		n2.createRelationshipTo(n, Rel.WATN);
		tx.success();
		ExecutionEngine engine = new ExecutionEngine(db);
		ExecutionResult result = engine.execute("start n=node(*) where n.name! = 'Aída' return n, n.name");
		Iterator<Node> nodes = result.columnAs("n");
		assertThat(nodes.hasNext(), is(true));
		Node node = nodes.next();
		assertThat(node, is(n));
		System.out.println(node.getProperty("name"));
	}
	
	private Node createTestNode(String name) {
		Transaction tx = db.beginTx();
		 
		Node n = null;
		try
		{
		    n = db.createNode();
		    n.setProperty( "name", name );
		    tx.success();
		}
		catch ( Exception e )
		{
		    tx.failure();
		}
		finally
		{
		    tx.finish();
		}
		return n;
	}
	
	enum Rel implements RelationshipType {
		WATN
	}

}
