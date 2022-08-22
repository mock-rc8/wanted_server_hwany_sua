package com.example.demo.src.position;

import com.example.demo.src.like.model.GetRewardRes;
import com.example.demo.src.position.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PositionDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetOpenPositionRes getPositionOpen(int jobIdx, int dutyIdx) {
        String getJobQuery = "select categoryIdx, category from EmploymentCategory where categoryIdx=?";
        String getJobParams = String.valueOf(jobIdx);

        JobCategory jobCategory = this.jdbcTemplate.queryForObject(getJobQuery,
                (rs, rowNum) -> new JobCategory(
                        rs.getInt("categoryIdx"),
                        rs.getString("category")),
                getJobParams);

        String getDutyQuery = "select subcategoryIdx, subcategory from EmploymentSubCategory where subcategoryIdx=?";
        String getDutyParams = String.valueOf(dutyIdx);

        DutyCategory dutyCategory = this.jdbcTemplate.queryForObject(getDutyQuery,
                (rs, rowNum) -> new DutyCategory(
                        rs.getInt("subcategoryIdx"),
                        rs.getString("subcategory")),
                getDutyParams);

        EmpRegion empRegion = new EmpRegion(5, "한국", 5, "전국");

        String getSearchCategory = "select searchcategoryIdx, searchcategory from SearchCategory";
        List<SearchCategory> searchCategories = this.jdbcTemplate.query(getSearchCategory,
                (rs, rowNum) -> new SearchCategory(
                        rs.getInt("searchcategoryIdx"),
                        rs.getString("searchcategory")));

        String getCompanyQuery = "select c.companyIdx, c.logoUrl, c.companyName from Company c ORDER BY RAND() LIMIT 5";
        List<Company> companies = this.jdbcTemplate.query(getCompanyQuery,
                (rs, rowNum) -> new Company(
                        rs.getInt("companyIdx"),
                        rs.getString("logoUrl"),
                        rs.getString("companyName")));

        for (Company c : companies) {
            String getThumbnailQuery = "select companyImg from CompanyImg where companyIdx=?";
            String getThumbnailParams = String.valueOf(c.getCompanyIdx());

            List<String> thumbnails = this.jdbcTemplate.query(getThumbnailQuery,
                    (rs, rowNum) -> new String (
                            rs.getString("companyImg")),
                    getThumbnailParams);

            if(thumbnails.size() != 0) {
                c.setThumbnail(thumbnails.get(0));
            }


            String getPositionQuery = "select COUNT(*) as position from Employment where companyIdx=?;";
            String getPositionParams = String.valueOf(c.getCompanyIdx());

            int position = this.jdbcTemplate.queryForObject(getPositionQuery,
                    (rs, rowNum) -> new Integer(
                            rs.getInt("position")),
                    getPositionParams);

            c.setPosition(position);
        }


        String getEmploymentQuery = "select e.employmentIdx, e.employment, c.companyName, r.name as region, n.name as nation from Employment e" +
                " JOIN Company c ON e.companyIdx = c.companyIdx JOIN EmpRegion er ON er.employmentIdx = e.employmentIdx " +
                "JOIN Nation n ON n.nationIdx = er.nationIdx JOIN Region r ON r.regionIdx = er.regionIdx " +
                "JOIN CategoryList cl ON cl.employmentIdx = e.employmentIdx " +
                "JOIN EmploymentSubCategory es ON es.subcategoryIdx = cl.subcategoryIdx " +
                "JOIN EmploymentCategory ec ON ec.categoryIdx = es.categoryIdx " +
                "WHERE cl.subcategoryIdx = ? and ec.categoryIdx = ?";
        Object[] getEmploymentParams = new Object[]{dutyIdx, jobIdx};

        List<Employment> employments = this.jdbcTemplate.query(getEmploymentQuery,
                (rs, rowNum) -> new Employment(
                        rs.getInt("employmentIdx"),
                        rs.getString("employment"),
                        rs.getString("companyName"),
                        rs.getString("region"),
                        rs.getString("nation")),
                        getEmploymentParams);

        for(Employment e : employments) {
            String getThumbnailQuery = "select employmentImg from EmploymentImg where employmentIdx=?";
            String getThumbnailParams = String.valueOf(e.getEmploymentIdx());

            List<String> thumbnails = this.jdbcTemplate.query(getThumbnailQuery,
                    (rs, rowNum) -> new String (
                            rs.getString("employmentImg")),
                    getThumbnailParams);

            e.setThumbnail(thumbnails.get(0));

            String getRewardQuery = "select e.applicant, e.recommender from Employment e where e.employmentIdx =?";
            String getRewardParams = String.valueOf(e.getEmploymentIdx());
            GetRewardRes getRewardRes = this.jdbcTemplate.queryForObject(getRewardQuery,
                    (rs, rowNum) -> new GetRewardRes(
                            rs.getString("applicant"),
                            rs.getString("recommender")),
                    getRewardParams);
            int reward = Integer.parseInt(getRewardRes.getApplicant()) + Integer.parseInt(getRewardRes.getRecommender());
            e.setReward("채용보상금 " + String.valueOf(reward) + "원");

            e.setIsBookmark(0);
        }

        return new GetOpenPositionRes(jobCategory, dutyCategory, empRegion, "전체", searchCategories, companies, employments);
    }
}