package com.cesar.sharing.entity;

import com.cesar.sharing.entity.auditing.Auditable;
import com.cesar.sharing.types.ResourceSharingStatus;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ResourceSharing extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "period_id")
    private Period period;

    /** The contract, brings the resource and CC (Cost Center) origin */
    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;

    /**
     * Represents the CC destini code. We don't need the hole CostCenter
     * object, we keep it simple
     **/
    @Column( nullable = false )
    private String costCenterCode;

    /**
     * CC origin approver's e-mail. In this case, we don't need
     * the hole Resource object, we keep it simple
     **/
    @Column( nullable = false )
    private String originApproverEmail;

    /**
     * CC destini approver's e-mail. In this case, we don't need
     * the hole CostCenter object, we keep it simple
     **/
    @Column( nullable = false )
    private String destiniApproverEmail;

    /** Status, must be one of those values @see {@link ResourceSharingStatus}, default OPENED */
    @Column( nullable = false, columnDefinition = "varchar(255) default 'OPENED'")
    private String status = ResourceSharingStatus.OPENED.toString();

    /** The allocation percent value */
    @Column( nullable = false )
    private Double percent;

    @Column()
    private String observation;

    /**
     * <p>
     *  Shortcut access method to get the resource email from current contract.
     * @return The resource email
     */
    public String getEmail() {
        Contract contract = Objects.requireNonNull(getContract());
        return contract.getEmail();
    }

    /**
     * <p>
     *  Shortcut method to set the resource email into the contract.
     * @param email The resource email
     */
    public void setContractByEmail(String email) {
        Contract contract = new Contract();
        Resource resource = new Resource();
        resource.setEmail(email);
        contract.setResource(resource);
        setContract(contract);
    }
}
