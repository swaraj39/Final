    package com.pack.demo.Repository;

    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.stereotype.Repository;
    import org.springframework.data.repository.query.Param;
    import com.pack.demo.ModelDAO.UserModel;

    @Repository
    public interface UserRepo extends JpaRepository<UserModel,String> {
        UserModel findByEmail(String email);


        @Query("SELECT u FROM UserModel u WHERE u.id = :ids")
        UserModel usersOne(@Param("ids") String ids);

    }
