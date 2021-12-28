import java.lang.Math;

class Point {
	public double x, y;
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public double dist(Point p) {
		return Math.sqrt(
			(this.x-p.x)*(this.x-p.x)
			+(this.y-p.y)*(this.y-p.y)
		);
	}
	public Point copy() {
		return new Point(this.x, this.y);
	}
}

class Record {
	public int id;
	public Point location;
	public double score;

	public Record(int id, Point location, double score) {
		this.id = id;
		this.location = location;
		this.score = score;
	}

	public Record copy() {
		return new Record(
			this.id, 
			this.location.copy(), 
			this.score
		);
	}
}

class Node {
	public Record poi;
	public Node next;

	public Node(Record poi) {
		this.poi = poi;
	}
}

class RankList {

	private Node first;
	private int nodeCount;

	public RankList() { }

	public int size() { return nodeCount; }

	public int insert(Record poi) {
		//Note: The code is taken from the book of Lafore "Data structures and algorithms in Java"
		//It is adopted to suit this program
		
		
		//This methos is a sorted insert, decreasing, based on the score!!!
		
		//Creating new node with record poi and creating two pointers for the list		
		
		Node newNode = new Node(poi);
		Node previous = null;
		Node current = this.first;
		
		//Searching where to insert the new node
		
		while ((current != null) && (poi.score < current.poi.score)) {
			
			previous = current;			
			current = current.next;			
		}
		//Checking whether we have to insert in the beginning of the list or not  		
		if(previous == null){
			first = newNode;
		}
		else {
			//Change the previous node to point as next to the newNode			
			previous.next = newNode;				
		}
		//Making  the newNode to point the next node of the rest list		
		newNode.next = current;	
		//Increasing the nodeCount and returning it		
		nodeCount = nodeCount + 1;
		return nodeCount;
	}
	
	public RankList nearest(Point p, int k) {
		/*The general idea is that we rewrite our list and instead of user's score 
		 * we have the distance as score. Then we take the first k nodes and return the results*/
				
		//Creating two list
		
		RankList res = new RankList();		
		RankList temp = new RankList();
		//Creating d which is a temporary variable for distance
		
		double d;
		//The insertion methods return integer, so I use this variable so that the method can return the integer
		
		int c = 0;
		
		//1.Creating a temp list sorted (increasingly)		
		Node parent = this.first;
		
		while (parent != null) {
			//Calculating the distance between location's point and the point of user
			d = parent.poi.location.dist(p);			
			//The distance here is like the score so we will make new ranklist in which we will have new scores	= distances		
			Record newRecord = new Record(parent.poi.id, parent.poi.location, d);
			//Add new record in temp list using increasing sorting insert			
			c = temp.incrInsert(newRecord);
			
			parent = parent.next;			
		}
		
		//2.Getting the first k and insert them in the res list (sorted)		
		parent = temp.first;
		while (parent != null && res.nodeCount < k) {
					
			c = res.incrInsert(parent.poi);			
			parent = parent.next;			
		}
		
		//If k > this.nodeCount then I have less records than is required, so I inform with a message
		
		if (this.nodeCount < k) {
			System.out.println("After searching, we found that our base does not have so many locations!\n");			
		}
		
		//Returning the result list
		return res;
	}

	public RankList nearest(Point p, double maxDist) {
		/*Here, we get all the nearest points
		 * nearest is implied by maxDist so we get those points within maxDist
		 * */
		
		//Creating result list, a pointer to list in which the method will be called and an integer for the nodeCount
		
		RankList res = new RankList();
		Node parent = this.first;
		int c = 0;
		
		
		while(parent != null) {
			if(parent.poi.location.dist(p) <= maxDist) {
				//If the location is within the maxDist we add it in the result list without sorting, O(1)
				
				c = res.simpleInsert(parent.poi);
			}
			
			parent = parent.next;
		}

		//Returning the result list		
		return res;
	}

	public RankList highScore(int k) {
		/*The general idea is that we take the first k nodes and return the results*/
		
		
		//Creating the result list, a pointer for the list with data and an integer which will 
		//accept the returning answers of insertion method
		
		RankList res = new RankList();		
		Node parent = this.first;
		int c = 0;		
		
		//Getting the first k and insert them in the res list (sorted)		
		while (parent != null && res.size() < k) {
			//In case of simple insertion, the results are sorted in decreasing way			
			c = res.insert(parent.poi);			
			parent = parent.next;			
		}
		//If k > this.nodeCount then I have less records than is required, so I inform with a message		
		if (res.nodeCount < k) {
			System.out.println("After searching, we found that our base does not have so many locations!\n");			
		}
		//Returning the result list			
		return res;
	}

	public RankList highScore(double minScore) {
		/*Here, we get all the farthest points
		 * farthest is implied by minScore so we get those points further than minScore
		 * */
		
		//Creating result list, a pointer to list in which the method will be called and an integer for the nodeCount
		
		RankList res = new RankList();
		int c = 0;
		Node parent = this.first;
		
		//For every place, compare the scores between location's score and minScore
		while(parent != null) {
			if (parent.poi.score >= minScore) {
				//If the score is bigger than minScore we add it in the result list without sorting				
				c = res.simpleInsert(parent.poi);
			}
			
			parent = parent.next;
		}
		//Returning the result list	
		
		return res;
	}

	public RankList inCommonWith(RankList rankList) {
		/*This method creates an intersection of two lists*/
		
		
		//Creating result list, two pointer for each list and an integer for the nodeCount
		
		RankList res = new RankList();
		int c = 0;
		Node parent = this.first;
		Node parentRL = rankList.first;
		
		//For every record of first list check which of all records of the second list has the same record id
		
		while (parentRL != null) {
			parent =  this.first;
			while (parent != null) {
				if (parent.poi.id == parentRL.poi.id) {
					//If the id is the same then add it to the result list					
					c = res.simpleInsert(parent.poi);
				}
				
				parent = parent.next;
			}
			
			parentRL = parentRL.next;
		}
		//Returning the result list			
		return res;
	}
			
	private int simpleInsert(Record poi) {		
		//Inserting data in the beginning of the list
		
		//Creating new node with record poi		
		Node newNode = new Node(poi);
		
		//Make new node to point the first node		
		newNode.next = first;
		//Update new node to be the first		
		first = newNode;
		//Increasing the nodeCount and returning it		
		nodeCount = nodeCount + 1;
		return nodeCount;
	}
	
	private int incrInsert(Record poi) {
		//Note: The code is taken from the book of Lafore "Data structures and algorithms in Java"
		//It is adopted to suit this program
		
		//This methos is a sorted insert, increasing, based on the score!!!
		
		//Creating new node with record poi and creating two pointers for the list				
		Node newNode = new Node(poi);
		Node previous = null;
		Node current = first;
		
		//Searching where to insert the new node		
		while ((current != null) && (poi.score > current.poi.score)) {
			
			previous = current;
			
			current = current.next;			
		}
		//Checking whether we have to insert in the beginning of the list or not  		
		if(previous == null){
			first = newNode;
		}
		else {
			//Change the previous node to point as next to the newNode			
			previous.next = newNode;				
		}
		//Making  the newNode to point the next node of the rest list		
		newNode.next = current;	
		//Increasing the nodeCount and returning it		
		nodeCount = nodeCount + 1;
		return nodeCount;
	}			
}
