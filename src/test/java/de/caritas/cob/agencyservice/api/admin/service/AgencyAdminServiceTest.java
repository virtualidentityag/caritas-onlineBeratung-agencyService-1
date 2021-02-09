package de.caritas.cob.agencyservice.api.admin.service;

import static de.caritas.cob.agencyservice.api.model.AgencyTypeRequestDTO.AgencyTypeEnum.DEFAULT_AGENCY;
import static de.caritas.cob.agencyservice.api.model.AgencyTypeRequestDTO.AgencyTypeEnum.TEAM_AGENCY;
import static de.caritas.cob.agencyservice.testHelper.TestConstants.AGENCY_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.reflect.Whitebox.setInternalState;

import de.caritas.cob.agencyservice.api.exception.httpresponses.BadRequestException;
import de.caritas.cob.agencyservice.api.exception.httpresponses.ConflictException;
import de.caritas.cob.agencyservice.api.exception.httpresponses.NotFoundException;
import de.caritas.cob.agencyservice.api.model.AgencyDTO;
import de.caritas.cob.agencyservice.api.model.AgencyTypeRequestDTO;
import de.caritas.cob.agencyservice.api.model.UpdateAgencyDTO;
import de.caritas.cob.agencyservice.api.repository.agency.Agency;
import de.caritas.cob.agencyservice.api.repository.agency.AgencyRepository;
import de.caritas.cob.agencyservice.api.service.LogService;
import java.util.Optional;
import org.jeasy.random.EasyRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class AgencyAdminServiceTest {

  @InjectMocks
  AgencyAdminService agencyAdminService;

  @Mock
  AgencyRepository agencyRepository;

  @Mock
  UserAdminService userAdminService;

  @Mock
  private Logger logger;

  @Before
  public void setup() {
    setInternalState(LogService.class, "LOGGER", logger);
  }

  @Test(expected = BadRequestException.class)
  public void saveAgency_Should_throwBadREquestException_When_consultingTypeInAgencyDtoDoesNotExist() {
    AgencyDTO agencyDTO = new EasyRandom().nextObject(AgencyDTO.class);
    agencyDTO.setConsultingType(-10);

    this.agencyAdminService.saveAgency(agencyDTO);
  }

  @Test(expected = NotFoundException.class)
  public void updateAgency_Should_ThrowNotFoundException_WhenAgencyIsNotFound() {

    when(agencyRepository.findById(AGENCY_ID)).thenReturn(Optional.empty());

    EasyRandom easyRandom = new EasyRandom();
    UpdateAgencyDTO updateAgencyDTO = easyRandom.nextObject(UpdateAgencyDTO.class);
    agencyAdminService.updateAgency(AGENCY_ID, updateAgencyDTO);

  }

  @Test(expected = NotFoundException.class)
  public void findAgencyById_Should_ThrowNotFoundException_WhenAgencyIsNotFound() {
    when(agencyRepository.findById(AGENCY_ID)).thenReturn(Optional.empty());

    agencyAdminService.findAgencyById(AGENCY_ID);
  }

  @Test(expected = NotFoundException.class)
  public void changeAgencyType_Should_throwNotFoundException_When_agencyWasNotFound() {
    when(agencyRepository.findById(AGENCY_ID)).thenReturn(Optional.empty());

    agencyAdminService.changeAgencyType(AGENCY_ID, mock(AgencyTypeRequestDTO.class));
  }

  @Test(expected = ConflictException.class)
  public void changeAgencyType_Should_throwConflictException_When_agencyHasAlreadyTypeToChange() {
    Agency agency = new EasyRandom().nextObject(Agency.class);
    when(agencyRepository.findById(AGENCY_ID)).thenReturn(Optional.of(agency));
    AgencyTypeRequestDTO requestDTO = new AgencyTypeRequestDTO().agencyType(TEAM_AGENCY);

    agencyAdminService.changeAgencyType(AGENCY_ID, requestDTO);
  }

  @Test
  public void changeAgencyType_Should_callUserAdminServiceAndSaveChangedAgency_When_agencyCanBeChanged() {
    Agency agency = new EasyRandom().nextObject(Agency.class);
    when(agencyRepository.findById(AGENCY_ID)).thenReturn(Optional.of(agency));
    AgencyTypeRequestDTO requestDTO = new AgencyTypeRequestDTO().agencyType(DEFAULT_AGENCY);

    agencyAdminService.changeAgencyType(AGENCY_ID, requestDTO);

    verify(this.userAdminService, times(1)).adaptRelatedConsultantsForChange(eq(AGENCY_ID),
        eq(requestDTO.getAgencyType().getValue()));
    verify(this.agencyRepository, times(1)).save(any());
  }

}
