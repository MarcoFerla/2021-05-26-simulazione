package it.polito.tdp.yelp.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private YelpDao dao;
	private SimpleDirectedWeightedGraph<Business,DefaultWeightedEdge> grafo;
	private Map<String,Business> idMap;
	
	public Model() {
		dao= new YelpDao();
		idMap= new HashMap<String,Business>();
		dao.getAllBusiness(idMap);
	}
	
	public void creaGrafo(String citta,int anno) {
		
		grafo = new SimpleDirectedWeightedGraph<Business,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.getVerici(citta, anno, idMap));
		
		for(Adiacenza a: dao.getAdiacenze(citta, anno, idMap)) {
			DefaultWeightedEdge e= grafo.getEdge(a.getB1(), a.getB2());
			if(e==null) {
				if(a.getPeso()>0) {
					Graphs.addEdgeWithVertices(grafo, a.getB1(), a.getB2(),a.getPeso());
				}else {
					Graphs.addEdgeWithVertices(grafo, a.getB2(), a.getB1(),Math.abs(a.getPeso()));
				}
			}
		}
	}
	
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
		
	}
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public Set<Business> getVertici(){
		return this.grafo.vertexSet();
	}
	
	public List<String> getCitta(){
		return this.dao.getAllCities();
	}
	
	public Business getLocaleMigliore() {
		Business best= null;
		double max=0.0;
		
		for(Business b:this.grafo.vertexSet()) {
			double val=0.0;
			for(DefaultWeightedEdge eIn: grafo.incomingEdgesOf(b)) {
				val+=grafo.getEdgeWeight(eIn);
			}
			for(DefaultWeightedEdge eOut: grafo.outgoingEdgesOf(b)) {
				val-=grafo.getEdgeWeight(eOut);
			}
			if(val>max) {
				best=b;
				max=val;
			}
		}
		
		return best;
	}
}
