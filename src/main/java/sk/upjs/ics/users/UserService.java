package sk.upjs.ics.users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {
    private final List<User> database;


    public UserService(List<User> database) {
        this.database = database;
    }

    public Map<User.Gender, Integer> countByGender() {
        var result = new HashMap<User.Gender, Integer>();

        for (var gender: User.Gender.values()) {
            result.put(gender, 0);
        }

        for (var user: this.database) {
            var genderCount = result.get(user.gender());
            result.put(user.gender(), genderCount + 1);
        }

        return result;
    }

    public Map<User.Gender, Double> genderRatios() {
        var counts = this.countByGender();
        var totalCount = this.database.size();

        var result = new HashMap<User.Gender, Double>();
        for (var count: counts.entrySet()) {
            result.put(count.getKey(), (double) count.getValue() / totalCount);
        }

        return result;
    }
}
