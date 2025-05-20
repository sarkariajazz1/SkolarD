package skolard.logic;

import java.util.List;

import skolard.objects.Session;
import skolard.objects.Tutor;

public class RatingList extends PriorityList<Session> {

    // Default constructor
    public RatingList() {
        super();
    }

    public List<Session> sortByBestCourseRating(String course){
        if(items.isEmpty()){ 
            return null; //Must change to something more appropriate
        } else if(items.size() > 1){
            
            filterSessionToCourse(course);

            //Iterates through list to sort sessions based on tutor grades in descending order
            for (int i = 0; i < items.size(); i++) {
                    for (int j = 0; j < items.size() - 1; j++) {
                        Session s1 = items.get(j);
                        Session s2 = items.get(j+1);
                        String tutorRatingOne = s1.getTutor().getGradeForCourse(course);
                        String tutorRatingTwo = s2.getTutor().getGradeForCourse(course);
                        

                        double ratingOne;
                        double ratingTwo;
                    
                        //Attempt to get the ratings and if not parsable, default rating is 1.0 for non-appropriate ratings
                        try{
                            ratingOne = Double.parseDouble(tutorRatingOne);
                        }catch (NumberFormatException e){
                            ratingOne = 1.0;
                        }

                        try{
                            ratingTwo = Double.parseDouble(tutorRatingTwo);
                        }catch (NumberFormatException e){
                            ratingTwo = 1.0;
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
        Session temp = items.get(i);
        items.set(i, items.get(j));
        items.set(j, temp);
    }

    private void filterSessionToCourse(String course){
        int index = 0;

        //Removes sessions that are not for the specified course
        while(index < items.size()){
            Session session = items.get(index);
            String tutorRating = session.getTutor().getGradeForCourse(course);
            if(tutorRating.equals("N/A")){
                items.remove(session);
            }else{
                index++;
            }
        }
    }

}
