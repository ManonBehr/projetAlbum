package fr.uga.miashs.album.service;


import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.update.*;

public class PictureAnnotationService {

	public static void main(String[] args) {
		
		
		// SPARQL Update
		UpdateRequest request = UpdateFactory.create("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
													"INSERT DATA {"+
													" <http://imss.univ-grenoble-alpes.fr/ns/album#Selfie> rdfs:subClassOf <http://imss.univ-grenoble-alpes.fr/ns/album#Picture> ."+
													" <http://imss.univ-grenoble-alpes.fr/ns/album#picture321> a <http://imss.univ-grenoble-alpes.fr/ns/album#Selfie> .}");

		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, "http://localhost:3030/ALBUM/update");
		up.execute();
		
		// SPARQL Query
		Query query = QueryFactory.create("SELECT ?s  WHERE {?s a <http://imss.univ-grenoble-alpes.fr/ns/album#Picture>.}");
		  try (QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/ALBUM/sparql",query)) {
		    ResultSet results = qexec.execSelect() ;
		    for ( ; results.hasNext() ; )
		    {
		      QuerySolution soln = results.nextSolution() ;
		      RDFNode x = soln.get("s") ;       // Get a result variable by name.
		      Resource r = soln.getResource("p") ; // Get a result variable - must be a resource
		      Literal l = soln.getLiteral("o") ;   // Get a result variable - must be a literal
		      System.out.println(x+" "+r+" "+l);
		    }
		  }
	}

}
