package Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Ranking Data provides the leaderboard with information of first three
 * top scores.
 */
public class RankingData {

	public static List list = new ArrayList();

	/**
	 * sortScore function sort the list from largest to smallest
	 */
	public void sortScore() {
		Collections.sort(list);
		Collections.reverse(list);
	}

	/**
	 * addScore add the score to the list that contains scores
	 * @param score is the score input, will be used in stage
	 */
	public void addScore(int score){
		// When there are spaces, add score into the list
		if(list == null || list.size() < 3){
			list.add(score);
			sortScore();
		} else {
			// When there is no space, if score is high enough
			// remove the last score and add and sort scores
			int lastNum = (int)list.get(list.size()-1);
			if(score > lastNum){
				list.remove(list.size()-1);
				list.add(score);
				sortScore();
			}
		}
	}

	/**
	 * get three top scores
	 * @return the list contains three top scores
	 */
	public List getTopThreeScore(){
		return list;
	}

}
