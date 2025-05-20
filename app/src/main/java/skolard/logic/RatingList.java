package skolard.logic;

import java.util.List;

import skolard.objects.Tutor;

public class RatingList extends PriorityList<Tutor> {

    // Default constructor
    public RatingList() {
        super();
    }

    public List<Tutor> sortByBestCourseRating(String course){
        if(items.isEmpty()){ 
            return null;
        } else if(items.size() > 1){
            for (int i = 0; i < items.size(); i++) {
                    for (int j = 0; j < items.size() - 1; j++) {

                        String tutorRatingOne = items.get(j).getGradeForCourse(course);
                        String tutorRatingTwo = items.get(j + 1).getGradeForCourse(course);
                        double ratingOne;
                        double ratingTwo;
                    
                        try{
                            ratingOne = Double.parseDouble(tutorRatingOne);
                        }catch (NumberFormatException e){
                            ratingOne = 0.0;
                        }

                        try{
                            ratingTwo = Double.parseDouble(tutorRatingTwo);
                        }catch (NumberFormatException e){
                            ratingTwo = 0.0;
                        }

                        if (ratingOne < ratingTwo) {
                            swap(j, j + 1);
                        }
                    }
                }
        }  
        return items;
    }

    //Swapping function for sorting algorithm
    private void swap(int i, int j) {
        Tutor temp = items.get(i);
        items.set(i, items.get(j));
        items.set(j, temp);
    }

}
