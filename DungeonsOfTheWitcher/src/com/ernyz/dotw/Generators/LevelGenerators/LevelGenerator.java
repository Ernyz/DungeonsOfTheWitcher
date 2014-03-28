package com.ernyz.dotw.Generators.LevelGenerators;

import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Model.Tiles.Tile;
import com.ernyz.dotw.View.SaveGame;

/**
 * Responsible for generation of single level. Results in a map of one level.
 * Many of these are combined to make a World with branches and stuff.
 * 
 * @author Ernyz
 */
public class LevelGenerator {
	
	//private static DrunkardWalkLevelGenerator drunkardWalkGenerator;

	public static Array<Tile> generateLevel(int levelNumber) {
		
		//Decide what type of level to generate and which algorithm to use
		//Also, decide if this level will have any branches
		//For now, just create DrunkardWalk level with no branches or any other special features.
		Array<Tile> result = DrunkardWalkLevelGenerator.generateDrunkardWalkLevel(levelNumber);
		
		return result;
	}
	
}
