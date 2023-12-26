package com.nhat.demoSpringbooRestApi.repositories;

import com.nhat.demoSpringbooRestApi.models.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Integer> {
	@Query("select st.value from Setting st where st.key = :name")
	Optional<String> getSettingProperty(@Param("name") String propertyName);
	@Modifying
	@Query("update Setting st set st.value = :value where st.key = :name")
	void updateSettingProperty(@Param("name") String propertyName, @Param("value") String value);
//    @Query("select st from Setting st where st.group = :group")
//    Set<Setting> getSettingGroup(@Param("group") String settingGroup);
	Optional<Setting> findByKey(String key);

}

