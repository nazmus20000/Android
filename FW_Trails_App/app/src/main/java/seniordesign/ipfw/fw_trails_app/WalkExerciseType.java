/**
 Copyright (C) 2016 Jared Perry, Jaron Somers, Warren Barnes, Scott Weidenkopf, and Grant Grimm
 Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 and associated documentation files (the "Software"), to deal in the Software without restriction,
 including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies\n
 or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package seniordesign.ipfw.fw_trails_app;


public class WalkExerciseType extends ExerciseType {
   private final int walkIcon = R.drawable.ic_directions_walk_black_18dp; // icon for walking.
   private final String exerciseType = "walk";
   private final double walkingMET = 3.8;


   public WalkExerciseType(){
      MET = walkingMET;
      this.duration = new Duration(0);
   }
   public WalkExerciseType(Duration duration){
      MET = walkingMET;
      this.duration = duration;
   }

   // Return the String representation of a run activity.
   public String getExerciseType(){
      return exerciseType;
   }

   // Returns the Duration object
   public Duration getDuration(){
      return duration;
   }

   // Calculate Calories for walk exercise
   public int calculateCaloriesBurned(double BMR){
      return super.calculateCaloriesBurned(BMR);
   }
   // Returns the MET value per the activity.
   public double getMETValue() {
      return MET;
   }

   // Returns the R.drawable.etc for the icon related to the exercise type.
   public int getIconResourceId() {
      return walkIcon;
   }
}
