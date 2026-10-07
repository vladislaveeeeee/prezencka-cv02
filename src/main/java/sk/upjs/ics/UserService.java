package sk.upjs.ics;

import java.util.List;

public class UserService {
    List <User> users;

    public UserService(List<User> users) {
        this.users = users;
    }

    GenderRatio calculateGenderRatio(){

        if (users == null || users.isEmpty()) {
            return new GenderRatio(0, 0, 0, 0);
        }

        int boys = 0, girls = 0, other = 0, unknown = 0;
        for (User u : users) {

           if (u == null || u.gender() == null) {
                unknown++;
                continue;
            }
            switch (u.gender()) {
                case MALE -> boys++;
                case FEMALE -> girls++;
                case UNKNOWN -> unknown++;
                case OTHER -> other++;
            }
        }


        double n = users.size();
        return new GenderRatio(boys / n, girls / n, unknown / n, other / n);
    }

}
