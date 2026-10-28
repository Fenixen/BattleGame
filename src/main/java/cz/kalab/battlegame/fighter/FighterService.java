package cz.kalab.battlegame.fighter;

import cz.kalab.battlegame.common.ConflictException;
import cz.kalab.battlegame.common.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FighterService {

    private final FighterRepository repository;

    public FighterService(FighterRepository repository) {
        this.repository = repository;
    }

    public List<FighterResponse> findAll() {
        return repository.findAll().stream()
                .map(FighterResponse::from)
                .toList();
    }

    public FighterResponse findById(Long id) {
        return FighterResponse.from(getEntity(id));
    }

    /**
     * Loads the entity itself - used by other services (e.g. the battle service).
     */
    public Fighter getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Fighter", id));
    }

    @Transactional
    public FighterResponse create(FighterRequest request) {
        if (repository.existsByNameIgnoreCase(request.name())) {
            throw duplicateName(request.name());
        }
        Fighter fighter = new Fighter(request.name(), request.type(), request.health(), request.damage(),
                request.defense(), request.mana(), request.magicDamage());
        return FighterResponse.from(repository.save(fighter));
    }

    @Transactional
    public FighterResponse update(Long id, FighterRequest request) {
        Fighter fighter = getEntity(id);
        if (repository.existsByNameIgnoreCaseAndIdNot(request.name(), id)) {
            throw duplicateName(request.name());
        }
        // no save() needed - the entity is managed and changes are flushed at the end of the transaction
        fighter.update(request.name(), request.type(), request.health(), request.damage(),
                request.defense(), request.mana(), request.magicDamage());
        return FighterResponse.from(fighter);
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(getEntity(id));
    }

    private ConflictException duplicateName(String name) {
        return new ConflictException("Fighter with name '%s' already exists".formatted(name));
    }
}
