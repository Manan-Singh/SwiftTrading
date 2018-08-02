import {Component, Input, OnInit} from '@angular/core';
import {NgbModal, ModalDismissReasons, NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {UserServiceService} from '../user-service.service';

@Component({
  selector: 'ngbd-modal-content',
  template: `    
    <div class="modal-header">
      <button type="button" class="close" aria-label="Close" (click)="activeModal.dismiss('Cross click')">
        <span aria-hidden="true">&times;</span>
      </button>
    </div>
    <div class="modal-body">
      <app-trade-chart></app-trade-chart>
      <app-trade-history></app-trade-history>
    </div>
    <div class="modal-footer">
      <button type="button" class="btn btn-outline-dark" (click)="activeModal.close('Close click')">Close</button>
    </div>
  `
})
export class ModalContent implements OnInit{
  constructor(public activeModal: NgbActiveModal ) { }

  ngOnInit() { }
}



@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.css']
})

export class ModalComponent implements OnInit {

  closeResult: string;
  constructor(private modalService: NgbModal, private userService: UserServiceService) { }

  ngOnInit() {
  }

  open(strategy:any){
    this.userService.passStrategy(strategy);
    const modalRef = this.modalService.open(ModalContent, {size: 'lg' })
      .result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return  `with: ${reason}`;
    }
  }

}


