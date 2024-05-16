package to.tinypota.railbots.api;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class RailGraph {
	private final Multimap<BlockPos, BlockPos> graph;
	
	public RailGraph() {
		graph = HashMultimap.create();
	}
	
	public void addRail(BlockPos source, BlockPos target) {
		graph.put(source, target);
		graph.put(target, source);
	}
	
	public void removeRail(BlockPos rail) {
		var connectedRails = graph.get(rail);
		for (var connectedRail : connectedRails) {
			graph.remove(connectedRail, rail);
		}
		graph.removeAll(rail);
	}
	
	public Multimap<BlockPos, BlockPos> getGraph() {
		return graph;
	}
	
	public Collection<BlockPos> getConnectedRails(BlockPos rail) {
		return graph.get(rail);
	}
	
	public NbtCompound toNbt(NbtCompound tag) {
		var connectionsTag = new NbtList();
		for (var rail : graph.keySet()) {
			var railTag = new NbtCompound();
			railTag.put("Pos", toPosNbt(rail));
			var connectedRailsTag = new NbtList();
			for (var connectedRail : graph.get(rail)) {
				connectedRailsTag.add(toPosNbt(connectedRail));
			}
			railTag.put("Connections", connectedRailsTag);
			connectionsTag.add(railTag);
		}
		tag.put("Connections", connectionsTag);
		return tag;
	}
	
	public void fromNbt(NbtCompound tag) {
		graph.clear();
		var connectionsTag = tag.getList("Connections", NbtElement.COMPOUND_TYPE);
		for (int i = 0; i < connectionsTag.size(); i++) {
			var railTag = connectionsTag.getCompound(i);
			var rail = fromPosNbt(railTag.getCompound("Pos"));
			var connectedRailsTag = railTag.getList("Connections", NbtElement.COMPOUND_TYPE);
			for (int j = 0; j < connectedRailsTag.size(); j++) {
				var connectedRail = fromPosNbt(connectedRailsTag.getCompound(j));
				graph.put(rail, connectedRail);
			}
		}
	}
	
	private NbtCompound toPosNbt(BlockPos pos) {
		var tag = new NbtCompound();
		tag.putInt("X", pos.getX());
		tag.putInt("Y", pos.getY());
		tag.putInt("Z", pos.getZ());
		return tag;
	}
	
	private BlockPos fromPosNbt(NbtCompound tag) {
		int x = tag.getInt("X");
		int y = tag.getInt("Y");
		int z = tag.getInt("Z");
		return new BlockPos(x, y, z);
	}
	
	public Deque<BlockPos> findPath(BlockPos source, BlockPos target) {
		if (!graph.containsKey(source) || !graph.containsKey(target)) {
			return new LinkedList<>();
		}
		
		var openSet = new PriorityQueue<>(Comparator.comparingDouble(Node::getTotalCost));
		var nodes = new HashMap<BlockPos, Node>();
		
		var startNode = new Node(source, null, 0.0, estimateCost(source, target));
		openSet.add(startNode);
		nodes.put(source, startNode);
		
		while (!openSet.isEmpty()) {
			var currentNode = openSet.poll();
			if (currentNode.getPosition().equals(target)) {
				return reconstructPath(currentNode);
			}
			
			for (var neighbor : graph.get(currentNode.getPosition())) {
				if (neighbor.getManhattanDistance(source) > 50) continue;
				
				var newCost = currentNode.getCostFromStart() + distance(currentNode.getPosition(), neighbor);
				var neighborNode = nodes.getOrDefault(neighbor, new Node(neighbor));
				if (newCost < neighborNode.getCostFromStart()) {
					neighborNode.setPrevious(currentNode);
					neighborNode.setCostFromStart(newCost);
					neighborNode.setTotalCost(newCost + estimateCost(neighbor, target));
					
					if (!openSet.contains(neighborNode)) {
						openSet.add(neighborNode);
					}
				}
				nodes.put(neighbor, neighborNode);
			}
		}
		
		return new LinkedList<>();
	}
	
	public Deque<BlockPos> findNearestPath(BlockPos source, BlockPos target) {
		if (!graph.containsKey(source)) {
			return new LinkedList<>(); // Empty stack
		}
		
		// Find the nearest node to the target position within 50 blocks
		BlockPos nearestNode = null;
		double minDistance = Double.MAX_VALUE;
		for (var node : graph.keySet()) {
			double distance = node.getManhattanDistance(target);
			if (distance < minDistance && node.getManhattanDistance(source) <= 50) {
				minDistance = distance;
				nearestNode = node;
			}
		}
		
		if (nearestNode == null) {
			return new LinkedList<>(); // Empty stack
		}
		
		var openSet = new PriorityQueue<>(Comparator.comparingDouble(Node::getTotalCost));
		var nodes = new HashMap<BlockPos, Node>();
		
		var startNode = new Node(source, null, 0.0, estimateCost(source, nearestNode));
		openSet.add(startNode);
		nodes.put(source, startNode);
		
		while (!openSet.isEmpty()) {
			var currentNode = openSet.poll();
			if (currentNode.getPosition().equals(nearestNode)) {
				return reconstructPath(currentNode);
			}
			
			for (var neighbor : graph.get(currentNode.getPosition())) {
				if (neighbor.getManhattanDistance(source) > 50) continue;
				
				double newCost = currentNode.getCostFromStart() + distance(currentNode.getPosition(), neighbor);
				var neighborNode = nodes.getOrDefault(neighbor, new Node(neighbor));
				if (newCost < neighborNode.getCostFromStart()) {
					neighborNode.setPrevious(currentNode);
					neighborNode.setCostFromStart(newCost);
					neighborNode.setTotalCost(newCost + estimateCost(neighbor, nearestNode));
					
					if (!openSet.contains(neighborNode)) {
						openSet.add(neighborNode);
					}
				}
				nodes.put(neighbor, neighborNode);
			}
		}
		
		return new LinkedList<>(); // Empty stack
	}
	
	private double distance(BlockPos start, BlockPos end) {
		return start.getManhattanDistance(end);
	}
	
	private double estimateCost(BlockPos start, BlockPos end) {
		return start.getManhattanDistance(end);
	}
	
	private Deque<BlockPos> reconstructPath(Node targetNode) {
		Deque<BlockPos> path = new LinkedList<>();
		var currentNode = targetNode;
		
		while (currentNode != null) {
			path.push(currentNode.getPosition());
			currentNode = currentNode.getPrevious();
		}
		
		return path;
	}
	
	private static class Node {
		private final BlockPos position;
		private Node previous;
		private double costFromStart;
		private double totalCost;
		
		public Node(BlockPos position) {
			this(position, null, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		}
		
		public Node(BlockPos position, Node previous, double costFromStart, double totalCost) {
			this.position = position;
			this.previous = previous;
			this.costFromStart = costFromStart;
			this.totalCost = totalCost;
		}
		
		public BlockPos getPosition() {
			return position;
		}
		
		public Node getPrevious() {
			return previous;
		}
		
		public void setPrevious(Node previous) {
			this.previous = previous;
		}
		
		public double getCostFromStart() {
			return costFromStart;
		}
		
		public void setCostFromStart(double costFromStart) {
			this.costFromStart = costFromStart;
		}
		
		public double getTotalCost() {
			return totalCost;
		}
		
		public void setTotalCost(double totalCost) {
			this.totalCost = totalCost;
		}
	}
}
