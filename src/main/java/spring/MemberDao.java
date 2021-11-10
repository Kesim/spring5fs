package spring;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class MemberDao {
	
	private JdbcTemplate jdbcTemplate;
	private RowMapper<Member> memRowMapper =
		new RowMapper<Member>() {
			@Override
			public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
				Member member = new Member(
						rs.getString("EMAIL"),
						rs.getString("PASSWORD"),
						rs.getString("NAME"),
						rs.getTimestamp("REGDATE").toLocalDateTime());
				member.setId(rs.getLong("ID"));
				return member;
			}
		};
	
	public MemberDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public Member selectByEmail(String email) {
		List<Member> results = jdbcTemplate.query(
				"select * from MEMBER where EMAIL = ?",
				memRowMapper,
				email);
		return results.isEmpty() ? null : results.get(0);
	}
	
	public void insert(Member member) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update((Connection con) -> {
			PreparedStatement pstmt = con.prepareStatement(
					"insert into MEMBER (EMAIL, PASSWORD, NAME, REGDATE) values (?, ?, ?, ?)",
					new String[] {"ID"}); //자동 생성되는 키 칼럼 목록 지정
			pstmt.setString(1, member.getEmail());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getName());
			pstmt.setTimestamp(4, Timestamp.valueOf(member.getRegisterDateTime()));
			return pstmt;
		}, keyHolder); //지정된 키 칼럼이 두 번째 파라미터에 저장됨
		Number keyValue = keyHolder.getKey();
		member.setId(keyValue.longValue()); //데이터 삽입 후 지정된 ID값이 객체에 저장되어 확인할 수 있음
	}
	
	public void update(Member member) {
		jdbcTemplate.update(
				"update MEMBER set NAME = ?, PASSWORD = ? where EMAIL = ?",
				member.getName(), member.getPassword(), member.getEmail());
	}

	public List<Member> selectAll() {
		List<Member> results = jdbcTemplate.query("select * from MEMBER",
				memRowMapper);
		return results;
	}
	
	public int count() {
		Integer count = jdbcTemplate.queryForObject( //결과가 한 행일 때 사용. 두 번째 파라미터는 반환값의 타입
				"select count(*) from MEMBER", Integer.class);
		return count;
	}
	
	public List<Member> selectByRegdate(LocalDateTime from, LocalDateTime to){
		List<Member> results = jdbcTemplate.query(
				"select * from MEMBER where REGDATE between ? and ? " + 
				"order by REGDATE desc",
				memRowMapper,
				from, to);
		return results;
	}
	
	public Member selectById(Long memId){
		List<Member> results = jdbcTemplate.query(
				"select * from MEMBER where ID = ?",
				memRowMapper, memId);
		return results.isEmpty() ? null : results.get(0);
	}
	
}
