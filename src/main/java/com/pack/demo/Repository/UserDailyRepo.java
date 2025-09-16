    package com.pack.demo.Repository;

    import java.util.List;

    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    import com.pack.demo.ModelDAO.UserDaily;

    @Repository
    public interface UserDailyRepo extends JpaRepository<UserDaily, Long>{
        List<UserDaily> findByUserId(String userId);   
    }
