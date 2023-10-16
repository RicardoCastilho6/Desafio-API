package br.com.webank.webank.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.webank.webank.dto.contaBancaria.ContaBancariaRequestDTO;
import br.com.webank.webank.dto.contaBancaria.ContaBancariaResponseDTO;

import br.com.webank.webank.model.ContaBancaria;




import br.com.webank.webank.repository.ContaBancariaRepository;

@Service
public class ContaBancariaService {
    
    @Autowired
    private ContaBancariaRepository contaBancariaRepository;

    
    @Autowired
    private ModelMapper ModelMapper;

    public List<ContaBancariaResponseDTO> obterTodos(){

        List<ContaBancaria> contasBancaria = contaBancariaRepository.findAll();

         return contasBancaria
            .stream()
            .map(contasBacaria -> ModelMapper.map(contasBacaria, ContaBancariaResponseDTO.class))
            .collect(Collectors.toList());


        
    }

    public ContaBancariaResponseDTO obterPorId(long id){
        Optional<ContaBancaria> optContaBancaria = contaBancariaRepository.findById(id);

        if(optContaBancaria.isEmpty()){
            throw new RuntimeException("Nenhum registro encontrado para o ID: " + id);
        }

        return ModelMapper.map(optContaBancaria.get(), ContaBancariaResponseDTO.class);
    }

    // O save serve tanto para adicionar quanto para atualizar.
    // se tiver id, ele atualiza, s enão tiver id ele adiciona.
    public ContaBancariaResponseDTO adicionar(ContaBancariaRequestDTO contaBancariaRequest){
       ContaBancaria contaBancariaModel = ModelMapper.map(contaBancariaRequest , ContaBancaria.class); 
        int id=0;
       contaBancariaModel.setId(id++);

       contaBancariaModel = contaBancariaRepository.save(contaBancariaModel); 

       return ModelMapper.map(contaBancariaModel, ContaBancariaResponseDTO.class);
    }

    public ContaBancariaResponseDTO atualizar(long id, ContaBancariaRequestDTO contaBancariaRequest){

        // Se não lançar exception é porque o cara existe no banco.
        obterPorId(id);

        ContaBancaria contaBancaria = ModelMapper.map(contaBancariaRequest, ContaBancaria.class);

        contaBancaria.setId(id);
        contaBancaria = contaBancariaRepository.save(contaBancaria);

        return ModelMapper.map(contaBancaria, ContaBancariaResponseDTO.class);
    }
   

    public void deletar(Long id){
        obterPorId(id);

        contaBancariaRepository.deleteById(id);
    }

}
