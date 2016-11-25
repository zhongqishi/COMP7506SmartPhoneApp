<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Game extends CI_Controller {

	/**
	 * Index Page for this controller.
	 *
	 * Maps to the following URL
	 * 		http://example.com/index.php/welcome
	 *	- or -
	 * 		http://example.com/index.php/welcome/index
	 *	- or -
	 * Since this controller is set as the default controller in
	 * config/routes.php, it's displayed at http://example.com/
	 *
	 * So any other public methods not prefixed with an underscore will
	 * map to /index.php/welcome/<method_name>
	 * @see https://codeigniter.com/user_guide/general/urls.html
	 */


	public function index()
	{

		$this->load->view('game');

	}


	public function isFightReady(){
		$json_string = $this->input->post();
		
		$this->load->model('game_model','',true);
		//传过来的是JSON String，用下面这句
    	//$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句
		$json = $json_string;

		$paramemter['RoomID'] = $json['RoomID'];
       
		$result = $this->game_model->isFightReady($paramemter);
		if($result['ret'] === 200)
		{
			$data['code'] = 0;	
			$data['RoomID'] = $result['RoomID'];
		}
		else{
			$data['code'] = 2;
			$data['message'] = "Waiting";		
		}
		
		$content_data['display_value']['Link']="http://i.cs.hku.hk/~zqshi/ci/index.php/Game/isFightReadyM";
		
		$content_data['display_value']['Input']=json_encode($json_string, true);

		$content_data['display_value']['Return']=json_encode($data);

		$this->load->view('result',$content_data);
	}

	public function setCards(){
		$json_string = $this->input->post();
		
		$this->load->model('game_model','',true);
		//传过来的是JSON String，用下面这句
    	//$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句
		$json = $json_string;

		$paramemter['RoomID'] = $json['RoomID'];
		$paramemter['UserID'] = $json['UserID'];
		$paramemter['CardID1'] = $json['CardID1'];
		$paramemter['CardID2'] = $json['CardID2'];
		$paramemter['CardID3'] = $json['CardID3'];

       
		$result = $this->game_model->setCards($paramemter);

		if($result['ret'] === 200)
		{
			$data['code'] = 0;	
			$data['RoomID'] = $result['RoomID'];
		}
		else{
			$data['code'] = 2;
			$data['message'] = "Waiting";		
		}
		
		$content_data['display_value']['Link']="http://i.cs.hku.hk/~zqshi/ci/index.php/Game/setCardsM";
		
		$content_data['display_value']['Input']=json_encode($json_string, true);

		$content_data['display_value']['Return']=json_encode($data);

		$this->load->view('result',$content_data);


	}

	public function isRoomReady(){
		$json_string = $this->input->post();
		
		$this->load->model('game_model','',true);
		//传过来的是JSON String，用下面这句
    	//$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句
		$json = $json_string;

		$paramemter['RoomID'] = $json['RoomID'];
       
		$result = $this->game_model->isRoomReady($paramemter);
		if($result['ret'] === 200)
		{
			$data['code'] = 0;	
			$data['RoomID'] = $result['RoomID'];
		}
		else{
			$data['code'] = 2;
			$data['message'] = "Waiting";		
		}
		
		$content_data['display_value']['Link']="http://i.cs.hku.hk/~zqshi/ci/index.php/Game/isRoomReadyM";
		
		$content_data['display_value']['Input']=json_encode($json_string, true);

		$content_data['display_value']['Return']=json_encode($data);

		$this->load->view('result',$content_data);
	}

	public function applyForFight()
	{
		$json_string = $this->input->post();
		
		$this->load->model('game_model','',true);
		//传过来的是JSON String，用下面这句
    	//$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句
    
		$json = $json_string;
		$paramemter['UserID'] = $json['UserID'];
       
		$result = $this->game_model->applyForFight($paramemter);
		if($result['ret'] === 200)
		{
			$data['code'] = 0;
			$data['RoomID'] = $result['RoomID'];	
		}
		else{
			$data['code'] = 2;
			$data['message'] = "Fail";		
		}
		
		$content_data['display_value']['Link']="http://i.cs.hku.hk/~zqshi/ci/index.php/Game/applyForFightM";
		
		$content_data['display_value']['Input']=json_encode($json_string, true);

		$content_data['display_value']['Return']=json_encode($data);

		$this->load->view('result',$content_data);
		
	}

	public function applyForFightM()
	{
		$json_string = $this->input->raw_input_stream;
		
		$this->load->model('game_model','',true);
		//传过来的是JSON String，用下面这句
    	$json = json_decode($json_string, true);
    	//传过来的是JSON Object，用下面这句 
		//$json = $json_string;
		$paramemter['UserID'] = $json['UserID'];
       
		$result = $this->game_model->applyForFight($paramemter);
		if($result['ret'] === 200)
		{
			$data['code'] = 0;
			$data['RoomID'] = $result['RoomID'];
			$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));	
		}
		else{
			$data['code'] = 2;
			$data['message'] = "Fail";	
			$this->output
	        			->set_content_type('application/json')
	        			->set_output(json_encode($data));	
		}
		
	}

}