package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.dto.BidListDTO;
import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BidListService {
    @Autowired
    private BidListRepository bidListRepository;

    public List<BidListDTO> getBidLists() {
        return bidListRepository.findAll()
                                .stream()
                                .map(bidList -> {
                                    BidListDTO bidListDTO = new BidListDTO();
                                    bidListDTO.setId(bidList.getId());
                                    bidListDTO.setAccount(bidList.getAccount());
                                    bidListDTO.setType(bidList.getType());
                                    bidListDTO.setBidQuantity(bidList.getBidQuantity());
                                    return bidListDTO;
                                })
                                .toList();
    }

    public BidList getBidList(Integer id) {
        return bidListRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(
                                        "La bidList avec l'id %d n'existe pas", id)));
    }

    public BidListDTO findBidListToUpdate(Integer id) {
        BidList bidList = getBidList(id);
        BidListDTO bidListDTO = new BidListDTO();
        bidListDTO.setId(bidList.getId());
        bidListDTO.setAccount(bidList.getAccount());
        bidListDTO.setType(bidList.getType());
        bidListDTO.setBidQuantity(bidList.getBidQuantity());
        return bidListDTO;
    }

    public void addBidList(BidListDTO bidListAddDTO) {
        BidList bidList = new BidList();
        bidList.setAccount(bidListAddDTO.getAccount());
        bidList.setType(bidListAddDTO.getType());
        bidList.setBidQuantity(bidListAddDTO.getBidQuantity());

        try {
            bidListRepository.save(bidList);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la création de la bidList");
        }
    }

    public void updateBidList(Integer id, BidListDTO bidListDTO) {
        BidList bidList = getBidList(id);
        bidList.setAccount(bidListDTO.getAccount());
        bidList.setType(bidListDTO.getType());
        bidList.setBidQuantity(bidListDTO.getBidQuantity());

        try {
            bidListRepository.save(bidList);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la modification de la bidList");
        }
    }

    public void deleteBidList(Integer id) {
        BidList bidList = getBidList(id);

        try {
            bidListRepository.delete(bidList);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                              "Une erreur est survenue lors de la suppression de la bidList");
        }
    }
}
