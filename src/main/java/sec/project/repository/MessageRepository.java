package sec.project.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sec.project.domain.Account;
import sec.project.domain.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByAccount(Account account);
}
