package com.domainlayer;

import com.dataaccesslayer.dao.crud.*;
import com.dataaccesslayer.entity.*;
import com.domainlayer.dto.problem.NewProblemDTO;
import com.domainlayer.dto.problem.ProblemDTO;
import com.domainlayer.dto.user.RegisterUserDTO;
import com.domainlayer.module.JwtException;
import com.domainlayer.module.JwtToken;

import java.util.*;

public class ProblemTM {
    private int problemReported = 0;
    private int attachmentCreated = 0;

    public int getProblemReported() {
        return problemReported;
    }
    public int getAttachmentCreated() {
        return attachmentCreated;
    }

    public Map<Object, Object> CreateNewProblem(final NewProblemDTO newProblemDTO, String token) {
        if (newProblemDTO.getTitle() == null || newProblemDTO.getSummary() == null ||
            newProblemDTO.getActualBehavior() == null || newProblemDTO.getExpectedBehavior() == null ||
            newProblemDTO.getDeploymentDomain() == null) {
            return Map.of(
                    "status", 400,
                    "error", "One of the including attributes is not provided (title, summary, actualBehavior, expectedBehavior)"
            );
        }
        DeploymentEntity deploymentEntity = null;
        try {
            deploymentEntity = new CrudDeploymentTDG().SelectByDomain(newProblemDTO.getDeploymentDomain());
        } catch (Exception ex) {
            return Map.of(
                    "status", 404,
                    "error", ex.getLocalizedMessage()
            );
        }
        UserEntity userEntity = null;
        Map registerUserCallback = null;
        if (newProblemDTO.getRegisterUserDTO() != null) {
            UserTM userTM = new UserTM();
            registerUserCallback = userTM.RegisterUser(new RegisterUserDTO(
                    newProblemDTO.getRegisterUserDTO().getEmail(),
                    newProblemDTO.getRegisterUserDTO().getPasswd(),
                    newProblemDTO.getRegisterUserDTO().getName(),
                    newProblemDTO.getRegisterUserDTO().getSurname()),
                    false
            );
            if ((int) registerUserCallback.get("status") != 201) {
                return registerUserCallback;
            }
            userEntity = new UserEntity(userTM.getUserRegisteredIds().get(userTM.getUserRegisteredIds().size() - 1));
        } else {
            Map decodedToken = (Map) JwtToken.DecodeToken(token.replace("Bearer ", "")).get("payload");
            try {
                userEntity = new CrudUserTDG().SelectByEmail((String) decodedToken.get("sub"));
            } catch (Exception ex) {
                return Map.of(
                        "status", 404,
                        "error", ex.getLocalizedMessage()
                );
            }
        }
        boolean commitProblem = true;
        if (newProblemDTO.getAttachments() != null) {
            commitProblem = false;
        }
        CrudProblemTDG crudProblemTDG = new CrudProblemTDG();
        crudProblemTDG.RegisterNew(new ProblemEntity(
                newProblemDTO.getTitle(),
                newProblemDTO.getSummary(),
                newProblemDTO.getConfiguration(),
                newProblemDTO.getExpectedBehavior(),
                newProblemDTO.getActualBehavior(),
                false,
                userEntity,
                deploymentEntity
        ));
        try {
            problemReported += crudProblemTDG.Commit(commitProblem);
        } catch (Exception ex) {
            return Map.of(
                    "status", 409,
                    "error", ex.getLocalizedMessage()
            );
        }
        if (!commitProblem) {
            List attachments = newProblemDTO.getAttachments();
            CrudAttachmentTDG crudAttachmentTDG = new CrudAttachmentTDG();
            ProblemEntity problemEntity = new ProblemEntity(
                    crudProblemTDG.getInsertedIds().get(crudProblemTDG.getInsertedIds().size() - 1)
            );
            attachments.forEach(attachment -> {
                byte[] attachmentByte = Base64.getDecoder().decode((String) attachment);
                crudAttachmentTDG.RegisterNew(new AttachmentEntity(attachmentByte, problemEntity));
            });
            try {
                attachmentCreated += crudAttachmentTDG.Commit(true);
            } catch (Exception ex) {
                return Map.of(
                        "status", 409,
                        "error", ex.getLocalizedMessage()
                );
            }
        }
        if (newProblemDTO.getRegisterUserDTO() != null) {
            return Map.of(
                    "status", 201,
                    "created", problemReported + attachmentCreated,
                    "token", registerUserCallback.get("token")
            );
        } else {
            return Map.of(
                    "status", 201,
                    "created", problemReported + attachmentCreated
            );
        }
    }

    public Map<Object, Object> AcceptProblem(final String token, final Integer problemId) {
        Object callback = CheckEmployee(token);
        if (callback instanceof Map) {
            return (Map) callback;
        }
        if (problemId == null) {
            return Map.of(
                    "status", 400,
                    "error", "Problem identifier is not provided"
            );
        }
        CrudProblemTDG crudProblemTDG = new CrudProblemTDG();
        ProblemEntity problemEntity = null;
        try {
            problemEntity = crudProblemTDG.SelectUnacceptedById(problemId);
        } catch (Exception ex) {
            return Map.of(
                    "status", 409,
                    "error", ex.getLocalizedMessage()
            );
        }
        problemEntity.setAccepted(true);
        System.out.println(problemEntity);
        crudProblemTDG.RegisterModified(problemEntity);
        try {
            problemReported += crudProblemTDG.Commit(true);
        } catch (Exception ex) {
            return Map.of(
                    "status", 409,
                    "error", ex.getLocalizedMessage()
            );
        }
        return Map.of(
                "status", 201,
                "created", problemReported
        );
    }

    public Map<Object, Object> CloseProblem(final String token, final Integer problemId) {
        Object callback = CheckEmployee(token);
        if (callback instanceof Map) {
            return (Map) callback;
        }
        if (problemId == null) {
            return Map.of(
                    "status", 400,
                    "error", "Problem identifier is not provided"
            );
        }
        ProblemEntity problemEntity = null;
        try {
            problemEntity = new CrudProblemTDG().SelectUnacceptedById(problemId);
        } catch (Exception ex) {
            return Map.of(
                    "status", 409,
                    "error", ex.getLocalizedMessage()
            );
        }
        CrudSolutionTDG crudSolutionTDG = new CrudSolutionTDG();
        Date actualTime = new Date(System.currentTimeMillis());
        crudSolutionTDG.RegisterNew(new SolutionEntity(
                actualTime,
                actualTime,
                0,
                problemEntity,
                (EmployeeEntity) callback
        ));
        try {
            problemReported += crudSolutionTDG.Commit(true);
        } catch (Exception ex) {
            return Map.of(
                    "status", 409,
                    "error", ex.getLocalizedMessage()
            );
        }
        return Map.of(
                "status", 201,
                "created", problemReported
        );
    }

    public Object ListAllUserProblems(final String token) {
        Object callback = CheckUser(token);
        if (callback instanceof Map) {
            return (Map) callback;
        }
        List<ProblemEntity> problemEntities = new ArrayList<>();
        try {
            System.out.println(((UserEntity) callback));
            problemEntities = new CrudProblemTDG().SelectAll(((UserEntity) callback).getEmail());
        } catch (Exception ex) {
            return Map.of(
                    "status", 404,
                    "error", ex.getLocalizedMessage()
            );
        }
        List<ProblemDTO> problemDTOList = new ArrayList<>();
        for (ProblemEntity problemEntity : problemEntities) {
            List<AttachmentEntity> attachmentEntities = new ArrayList<>();
            List<String> attachmentData = new ArrayList<>();
            try {
                attachmentEntities = new CrudAttachmentTDG().SelectAllById(problemEntity.getId());
                for (AttachmentEntity attachmentEntity : attachmentEntities) {
                    attachmentData.add(Base64.getEncoder().encodeToString(attachmentEntity.getData()));
                }
            } catch (Exception ex) {
                attachmentData = null;
            }
            problemDTOList.add(new ProblemDTO(
                    problemEntity.getId(),
                    problemEntity.getTitle(),
                    problemEntity.getSummary(),
                    problemEntity.getConfiguration(),
                    problemEntity.getExpectedBehavior(),
                    problemEntity.getActualBehavior(),
                    attachmentData));
        }
        return problemDTOList;
    }

    public Object ListUnresolvedProblems() {
        List<ProblemEntity> problemEntities = new ArrayList<>();
        try {
            problemEntities = new CrudProblemTDG().SelectAllUnresolved();
        } catch (Exception ex) {
            return Map.of(
                    "status", 404,
                    "error", ex.getLocalizedMessage()
            );
        }
        List<ProblemDTO> problemDTOList = new ArrayList<>();
        for (ProblemEntity problemEntity : problemEntities) {
            List<AttachmentEntity> attachmentEntities = new ArrayList<>();
            List<String> attachmentData = new ArrayList<>();
            try {
                attachmentEntities = new CrudAttachmentTDG().SelectAllById(problemEntity.getId());
                for (AttachmentEntity attachmentEntity : attachmentEntities) {
                    attachmentData.add(Base64.getEncoder().encodeToString(attachmentEntity.getData()));
                }
            } catch (Exception ex) {
                attachmentData = null;
            }
            problemDTOList.add(new ProblemDTO(
                    problemEntity.getId(),
                    problemEntity.getTitle(),
                    problemEntity.getSummary(),
                    problemEntity.getConfiguration(),
                    problemEntity.getExpectedBehavior(),
                    problemEntity.getActualBehavior(),
                    attachmentData));
        }
        return problemDTOList;
    }

    public Object ListUnacceptedProblems(final String token) {
        Object callback = CheckEmployee(token);
        if (callback instanceof Map) {
            return (Map) callback;
        }
        List<ProblemEntity> problemEntities = new ArrayList<>();
        try {
            problemEntities = new CrudProblemTDG().SelectAllUnaccepted();
        } catch (Exception ex) {
            return Map.of(
                    "status", 404,
                    "error", ex.getLocalizedMessage()
            );
        }
        List<ProblemDTO> problemDTOList = new ArrayList<>();
        for (ProblemEntity problemEntity : problemEntities) {
            List<AttachmentEntity> attachmentEntities = new ArrayList<>();
            List<String> attachmentData = new ArrayList<>();
            try {
                attachmentEntities = new CrudAttachmentTDG().SelectAllById(problemEntity.getId());
                for (AttachmentEntity attachmentEntity : attachmentEntities) {
                    attachmentData.add(Base64.getEncoder().encodeToString(attachmentEntity.getData()));
                }
            } catch (Exception ex) {
                attachmentData = null;
            }
            problemDTOList.add(new ProblemDTO(
                    problemEntity.getId(),
                    problemEntity.getTitle(),
                    problemEntity.getSummary(),
                    problemEntity.getConfiguration(),
                    problemEntity.getExpectedBehavior(),
                    problemEntity.getActualBehavior(),
                    attachmentData));
        }
        return problemDTOList;
    }

    private Object CheckEmployee(final String token) {
        String email = null;
        try {
            email = JwtToken.ValidateToken(token.replace("Bearer ", ""));
        } catch (JwtException ex) {
            return ex.getMyMessage();
        }
        EmployeeEntity employeeEntity = null;
        try {
            employeeEntity = new CrudEmployeeTDG().SelectByEmail(email);
        } catch (Exception ex) {
            return Map.of(
                    "status", 409,
                    "error", "You don't have permission for this resource"
            );
        }
        return employeeEntity;
    }

    private Object CheckUser(final String token) {
        String email = null;
        try {
            email = JwtToken.ValidateToken(token.replace("Bearer ", ""));
        } catch (JwtException ex) {
            return ex.getMyMessage();
        }
        UserEntity userEntity = null;
        try {
            userEntity = new CrudUserTDG().SelectByEmail(email);
        } catch (Exception ex) {
            return Map.of(
                    "status", 409,
                    "error", "You don't have permission for this resource"
            );
        }
        return userEntity;
    }
}
