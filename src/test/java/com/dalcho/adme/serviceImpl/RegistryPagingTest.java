package com.dalcho.adme.serviceImpl;

import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.repository.RegistryRepository;
import com.dalcho.adme.service.Impl.RegistryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegistryPagingTest {
    @Mock
    RegistryRepository registryRepository;
    @Mock
    Page<Registry> registryPage;
    @InjectMocks
    RegistryServiceImpl registryService;
    int curPage = 1;

    @Test
    void paging() throws Exception {
        when(registryRepository.findAllByOrderByCreatedAtDesc(any(Pageable.class))).thenReturn(registryPage);
        registryService.getBoards(curPage);
        verify(registryRepository).findAllByOrderByCreatedAtDesc(any(Pageable.class));
    }
}
// https://stackoverflow.com/questions/57045711/how-to-mock-pageable-object-using-mockito