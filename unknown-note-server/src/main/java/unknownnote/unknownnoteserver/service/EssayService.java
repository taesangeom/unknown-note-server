package unknownnote.unknownnoteserver.service;

import unknownnote.unknownnoteserver.dto.EssayDTO;
import unknownnote.unknownnoteserver.entity.EssayEntity;
import unknownnote.unknownnoteserver.repository.EssayRepository;
import unknownnote.unknownnoteserver.repository.UserRepository;
import unknownnote.unknownnoteserver.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        List<EssayEntity> essayEntities = essayRepository.findAll();
        return essayEntities.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    public EssayDTO createEssay(EssayDTO essayDTO) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(essayDTO.getUserId());
        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();
            EssayEntity essayEntity = convertDTOToEntity(essayDTO);
            essayEntity.setUser(userEntity);
            EssayEntity savedEntity = essayRepository.save(essayEntity);
            return convertEntityToDTO(savedEntity);
        }
        return null;
    }

    public EssayDTO getEssayById(Long essayId) {
        Optional<EssayEntity> optionalEssayEntity = essayRepository.findById(essayId);
        return optionalEssayEntity.map(this::convertEntityToDTO).orElse(null);
    }

    public EssayDTO updateEssay(Long essayId, EssayDTO updatedEssayDTO) {
        Optional<EssayEntity> optionalEssayEntity = essayRepository.findById(essayId);
        if (optionalEssayEntity.isPresent()) {
            EssayEntity essayEntity = optionalEssayEntity.get();
            EssayEntity updatedEntity = essayRepository.save(essayEntity);
            return convertEntityToDTO(updatedEntity);
        } else {
            return null;
        }
    }

    public boolean deleteEssay(Long essayId) {
        Optional<EssayEntity> optionalEssayEntity = essayRepository.findById(essayId);
        if (optionalEssayEntity.isPresent()) {
            essayRepository.deleteById(essayId);
            return true;
        } else {
            return false;
        }
    }

    private EssayDTO convertEntityToDTO(EssayEntity essayEntity) {
        EssayDTO essayDTO = new EssayDTO();
        essayDTO.setEssayId(essayEntity.getEssayId());
        essayDTO.setETitle(essayEntity.getETitle());
        essayDTO.setEContent(essayEntity.getEContent());
        essayDTO.setETime(essayEntity.getETime());
        essayDTO.setELikes(essayEntity.getELikes());
        essayDTO.setECategory(essayEntity.getECategory());

        essayDTO.setUserId(essayEntity.getUser().getUserId());
        return essayDTO;
    }

    private EssayEntity convertDTOToEntity(EssayDTO essayDTO) {
        EssayEntity essayEntity = new EssayEntity();
        essayEntity.setETitle(essayDTO.getETitle());
        essayEntity.setEContent(essayDTO.getEContent());
        essayEntity.setELikes(essayDTO.getELikes());
        essayEntity.setECategory(essayDTO.getECategory());

        return essayEntity;
    }
}
