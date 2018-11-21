package org.ncpsb.phoenixcluster.enhancer.webservice.model;

import org.ncpsb.phoenixcluster.enhancer.webservice.utils.ClusterUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AnalysisJobRowMapper implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        AnalysisJob analysisJob1 = new AnalysisJob();
        analysisJob1.setId(rs.getInt("ID"));
        analysisJob1.setFilePath(rs.getString("FILE_PATH"));
        analysisJob1.setUploadDate(rs.getString("UPLOAD_DATE"));
        analysisJob1.setUserId(rs.getInt("USER_ID"));
        analysisJob1.setStatus(rs.getString("STATUS"));
        analysisJob1.setToken(rs.getString("TOKEN"));
        analysisJob1.setPublic(rs.getBoolean("ISPUBLIC"));
        analysisJob1.setEmailAdd(rs.getString("EMAIL_ADD"));
        analysisJob1.setEmailSent(rs.getBoolean("IS_EMAIL_SENT"));
        analysisJob1.setAccessionId(rs.getString("ACCESSION"));
        return analysisJob1;
    }
}
