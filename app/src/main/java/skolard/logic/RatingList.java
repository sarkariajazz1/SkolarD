package skolard.logic;

import java.util.List;

import skolard.objects.Session;

public class RatingList extends PriorityList<Session> {

    // Default constructor
    public RatingList() {
        super();
    }

    public List<Session> sortByBestCourseRating(String course){
        for (int i = 0; i < items.size(); i++) {
                for (int j = 0; j < items.size() - 1; j++) {
                    Session s1 = items.get(j);
                    Session s2 = items.get(j + 1);
                    String tutorRatingOne = s1.getTutor().getGradeForCourse(course);
                    String tutorRatingTwo = s2.getTutor().getGradeForCourse(course);
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
        return items;
    }

    //Swapping function for sorting algorithm
    private void swap(int i, int j) {
        Session temp = items.get(i);
        items.set(i, items.get(j));
        items.set(j, temp);
    }

}
