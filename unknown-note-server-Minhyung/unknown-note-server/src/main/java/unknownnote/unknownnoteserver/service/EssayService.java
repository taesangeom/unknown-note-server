package unknownnote.unknownnoteserver.service;

import unknownnote.unknownnoteserver.dto.EssayDTO;
import unknownnote.unknownnoteserver.entity.EssayEntity;
import unknownnote.unknownnoteserver.repository.EssayRepository;
import unknownnote.unknownnoteserver.repository.UserRepository;
import unknownnote.unknownnoteserver.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EssayService {

    private final EssayRepository essayRepository;
    private final UserRepository userRepository;

    @Autowired
    public EssayService(EssayRepository essayRepository, UserRepository userRepository) {
        this.essayRepository = essayRepository;
        this.userRepository = userRepository;
    }

    public List<EssayDTO> getAllEssays() {
        return essayRepository.findAll().stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    public EssayDTO createEssay(EssayDTO essayDTO) {
        EssayEntity essayEntity = convertDTOToEntity(essayDTO);
        UserEntity userEntity = userRepository.findById(essayDTO.getUserId()).orElse(null);
        if (userEntity != null) {
            essayEntity.setUser(userEntity);
            EssayEntity savedEntity = essayRepository.save(essayEntity);
            return convertEntityToDTO(savedEntity);
        }
        return null;
    }

    private EssayDTO convertEntityToDTO(EssayEntity essayEntity) {
        // Conversion logic from EssayEntity to EssayDTO
    }

    private EssayEntity convertDTOToEntity(EssayDTO essayDTO) {
        // Conversion logic from EssayDTO to EssayEntity
    }

    // 추가
}
