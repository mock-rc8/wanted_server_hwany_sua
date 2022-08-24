package com.example.demo.src.resume;

import com.example.demo.src.resume.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ResumeDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<GetResumeListRes> getResumeList(int userIdx){

        String getResumeListQuery = "SELECT resumeIdx,title,DATE_FORMAT(updatedAt,'%Y.%m.%d') AS updatedAt,\n" +
                "       CASE WHEN status = 'ACTIVE' THEN '작성 완료'\n" +
                "            END AS status,\n" +
                "       CASE WHEN (language) = 0 THEN '한'\n" +
                "            ELSE 'A' END AS language FROM Resume WHERE userIdx = ? AND status = 'ACTIVE';";

        int getResumeListParams = userIdx;

        return this.jdbcTemplate.query(getResumeListQuery,
                (rs,rowNum)-> new GetResumeListRes(
                        rs.getInt("resumeIdx"),
                        rs.getString("title"),
                        rs.getString("updatedAt"),
                        rs.getString("status"),
                        rs.getString("language")),
                getResumeListParams);
    }


    // 이력서 상세 조회

    public GetResumeDetailRes getResumeDetail(int userIdx, int resumeIdx){

        System.out.println("이력서상세조회");

        // Resume
        String getResumeIntroQuery = "SELECT resumeIdx,language,title,U.name,U.email,U.phone,introduce,\n" +
                "CASE WHEN language = 0 THEN '한국어'\n" +
                "ELSE '영어' END AS language FROM Resume AS R\n" +
                "JOIN User AS U ON U.userIdx = R.userIdx WHERE R.userIdx = ? AND resumeIdx =?;";

        Object[] getResumeDetailParams = new Object[]{userIdx,resumeIdx};


        Resume resumeIntro = this.jdbcTemplate.queryForObject(getResumeIntroQuery,
                (rs,rowNum)-> new Resume(
                        rs.getInt("resumeIdx"),
                        rs.getString("language"),
                        rs.getString("title"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("introduce")),
                getResumeDetailParams);

        // Career

        String getCareerQuery = "SELECT careerIdx, company, department, isPresent, startedAt, endAt FROM Career AS C\n" +
                "WHERE resumeIdx =?;";

        List<Career> careerList = this.jdbcTemplate.query(getCareerQuery,
                (rs,rowNum)-> new Career(
                        rs.getInt("careerIdx"),
                        rs.getString("company"),
                        rs.getString("department"),
                        rs.getInt("isPresent"),
                        rs.getString("startedAt"),
                        rs.getString("endAt")),
                resumeIdx);

        // CareerResult

        String getCareerResultQuery = "SELECT R.careerIdx,R.resultIdx,  R.title, R.startedAt, R.endAt, R.content FROM Result AS R\n" +
                "JOIN Career AS C ON C.careerIdx = R.careerIdx WHERE C.resumeIdx =?;";

        List<CareerResult> careerResultList = this.jdbcTemplate.query(getCareerResultQuery,
                (rs,rowNum)-> new CareerResult(
                        rs.getInt("careerIdx"),
                        rs.getInt("resultIdx"),
                        rs.getString("title"),
                        rs.getString("startedAt"),
                        rs.getString("endAt"),
                        rs.getString("content")),
                resumeIdx);

        // Education

        String getEduQuery = "SELECT educationIdx, name AS university, major, study, isPresent, startedAt, endAt FROM Education WHERE resumeIdx = ?;";

        List<Education> educationList = this.jdbcTemplate.query(getEduQuery,
                (rs,rowNum)-> new Education(
                        rs.getInt("educationIdx"),
                        rs.getString("university"),
                        rs.getString("major"),
                        rs.getString("study"),
                        rs.getInt("isPresent"),
                        rs.getString("startedAt"),
                        rs.getString("endAt")),
                resumeIdx);

        // ResumeSkill

        String getResumeSkillQuery = "SELECT RS.skillIdx,S.name AS skill FROM ResumeSkill AS RS\n" +
                "JOIN Skill AS S ON RS.skillIdx = S.skillIdx WHERE RS.resumeIdx = ?;\n";

        List<ResumeSkill> resumeSkillList = this.jdbcTemplate.query(getResumeSkillQuery,
                (rs,rowNum)-> new ResumeSkill(
                        rs.getInt("skillIdx"),
                        rs.getString("skill")),
                resumeIdx);

        // Award

        String getAwardQuery = "SELECT awardsIdx,createdAt,title,content FROM Awards WHERE resumeIdx = ?;";

        List<Award> awardList = this.jdbcTemplate.query(getAwardQuery,
                (rs,rowNum)-> new Award(
                        rs.getInt("awardsIdx"),
                        rs.getString("createdAt"),
                        rs.getString("title"),
                        rs.getString("content")),
                resumeIdx);


        //ForeignLanguage

        String getFLanguageQuery = "SELECT flIdx, L.nation AS language, level FROM ForeignLanguage AS FL\n" +
                "                            JOIN Language AS L ON FL.languageIdx = L.languageIdx WHERE resumeIdx = ?;";

        List<ForeignLanguage> foreignLanguageList = this.jdbcTemplate.query(getFLanguageQuery,
                (rs,rowNum)-> new ForeignLanguage(
                        rs.getInt("flIdx"),
                        rs.getString("language"),
                        rs.getInt("level")),
                resumeIdx);

        // LanguageTest

        String getTestQuery = "\n" +
                "SELECT T.flIdx ,testIdx,title,score,createdAt FROM Test AS T\n" +
                "JOIN ForeignLanguage AS FL ON FL.flIdx = T.flIdx WHERE FL.resumeIdx = ?;";

        List<LanguageTest> languageTestList = this.jdbcTemplate.query(getTestQuery,
                (rs,rowNum)-> new LanguageTest(
                        rs.getInt("flIdx"),
                        rs.getInt("testIdx"),
                        rs.getString("title"),
                        rs.getString("score"),
                        rs.getString("createdAt")),
                resumeIdx);


        // Link

        String getLinkQuery = "SELECT linkIdx,url AS linkUrl FROM Link WHERE resumeIdx = ?;";

        List<ResumeLink> resumeLinkList = this.jdbcTemplate.query(getLinkQuery,
                (rs,rowNum) -> new ResumeLink(
                        rs.getInt("linkIdx"),
                        rs.getString("linkUrl")),
                resumeIdx);


        return new GetResumeDetailRes(resumeIntro,careerList,careerResultList,educationList,resumeSkillList,awardList,foreignLanguageList,languageTestList,resumeLinkList);

    }
}