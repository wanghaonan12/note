package com.example.exercises.mapper;

import com.example.exercises.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BrandMapper {
    @Select("SELECT * FROM tb_brand")
    List<Brand> selectAll();

    @Insert("INSERT INTO tb_brand VALUES (NULL,#{brandName},#{companyName},#{ordered},#{description},#{status})")
    void add(Brand brand);

    /**
     *
     * @param ids
     */
    void deleteByIds(@Param( "ids") int[] ids );

    @Select("SELECT* FROM tb_brand LIMIT #{begin},#{size}")
    List<Brand> selectByPage(@Param("begin") int begin, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM tb_brand")
    int selectTotalCount();

    List<Brand> selectByPageAndCondition( @Param( "begin") int begin, @Param( "size") int size,@Param("brand") Brand brand);
    int selectTotalCountByCondition(Brand brand);
}
